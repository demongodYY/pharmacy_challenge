package com.pharma.prescription.service;

import com.pharma.prescription.dto.PrescriptionDto;
import com.pharma.prescription.dto.PrescriptionItemRequestDto;
import com.pharma.prescription.dto.PrescriptionRequestDto;
import com.pharma.prescription.entity.*;
import com.pharma.prescription.entity.enumration.AuditLogStatus;
import com.pharma.prescription.entity.enumration.PrescriptionStatus;
import com.pharma.prescription.exception.BusinessRuleException;
import com.pharma.prescription.exception.ResourceNotFoundException;
import com.pharma.prescription.repository.*;
import com.pharma.prescription.shared.DataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.pharma.prescription.exception.ErrorMessages.*;

@Service
@RequiredArgsConstructor
public class PrescriptionService {
  private final PrescriptionRepository prescriptionRepository;
  private final PrescriptionItemRepository prescriptionItemRepository;
  private final PrescriptionStatusService prescriptionStatusService;
  private final PharmacyRepository pharmacyRepository;
  private final DrugRepository drugRepository;
  private final PharmacyDrugAllocationRepository allocationRepository;
  private final DataMapper dataMapper;
  private final AuditLogService auditLogService;

  @Transactional
  public PrescriptionDto create(PrescriptionRequestDto prescriptionRequestDto) {
    if (prescriptionRequestDto.getPrescriptionItems().isEmpty()) {
      throw new BusinessRuleException(getPrescriptionItemsCountMessage());
    }

    Pharmacy pharmacy = pharmacyRepository.findByPharmacyId(prescriptionRequestDto.getPharmacyId())
            .orElseThrow(() -> new ResourceNotFoundException(getNotFoundMessage("Pharmacy", prescriptionRequestDto.getPharmacyId())));
    Prescription prescription = new Prescription(prescriptionRequestDto.getPatientId(), pharmacy);
    Prescription savedPrescription = prescriptionRepository.save(prescription);

    for (PrescriptionItemRequestDto itemRequest : prescriptionRequestDto.getPrescriptionItems()) {
      Drug drug = drugRepository.findByDrugId(itemRequest.getDragId())
              .orElseThrow(() -> new ResourceNotFoundException(getNotFoundMessage("Drug", itemRequest.getDragId())));

      // 1. Check Drug Expiry
      if (drug.isExpired()) {
        throw new BusinessRuleException(getDrugExpiredMessage(drug));
      }

      // 2. Check Pharmacy Contract and Allocation
      PharmacyDrugAllocation allocation = allocationRepository
              .findByPharmacyPublicIdAndDrugPublicId(pharmacy.getPharmacyId(), drug.getDrugId())
              .orElseThrow(() -> new BusinessRuleException(
                      getNotAllocationMessage(itemRequest.getDragId(), pharmacy.getPharmacyId())
              ));
      if (!allocation.isContracted()) {
        throw new BusinessRuleException(getDrugNotContractedMessage(drug));
      }
      if (itemRequest.getDosage() > allocation.getAllocated()) {
        throw new BusinessRuleException(getDosageExceedsAllocatedMessage(itemRequest.getDosage()));
      }

      PrescriptionItem pItem = new PrescriptionItem(savedPrescription, drug, itemRequest.getDosage());
      prescriptionItemRepository.save(pItem);
    }
    return dataMapper.toPrescriptionDto(savedPrescription);
  }

  @Transactional
  public PrescriptionDto fulfillPrescription(UUID prescriptionId) {
    Prescription prescription = prescriptionRepository.findByPrescriptionId(prescriptionId)
            .orElseThrow(() -> new ResourceNotFoundException(getNotFoundMessage("Prescription", prescriptionId)));

    if (prescription.getStatus() != PrescriptionStatus.PENDING) {
      throw new BusinessRuleException(getPrescriptionStatusErrorMessage(prescription));
    }


    List<Drug> drugsToUpdate = new ArrayList<>();
    String requestedJson = toRequestDrugs(prescription.getPrescriptionItems());

    for (PrescriptionItem item : prescription.getPrescriptionItems()) {
      Drug drug = item.getDrug();
      if (item.getDosage() > drug.getStock()) {
        prescriptionStatusService.updatePrescriptionStatus(prescription, PrescriptionStatus.FAILED_FULFILLMENT);
        auditLogService.logAttempt(prescription.getPrescriptionId(), prescription.getPatientId(), prescription.getPharmacy().getPharmacyId(),
                requestedJson, null, AuditLogStatus.FAILURE, getFulfilmentStockOverflowFailureMessage(item, drug));
        throw new BusinessRuleException(getFulfilmentStockOverflowFailureMessage(item, drug));
      } else {
        drug.setStock(drug.getStock() - item.getDosage());
        drugsToUpdate.add(drug);
      }
    }

    // All good, update stocks and prescription
    try {
      drugRepository.saveAll(drugsToUpdate);
    } catch (ObjectOptimisticLockingFailureException e) {
      prescriptionStatusService.updatePrescriptionStatus(prescription, PrescriptionStatus.FAILED_FULFILLMENT);
      auditLogService.logAttempt(prescription.getPrescriptionId(), prescription.getPatientId(), prescription.getPharmacy().getPharmacyId(),
              requestedJson, null, AuditLogStatus.FAILURE, getFulfilmentConcurrentFailedMessage());
      throw new BusinessRuleException(getFulfilmentConcurrentFailedMessage(), e);
    }

    prescription.setStatus(PrescriptionStatus.FULFILLED);
    prescription.setFulfillmentDate(LocalDateTime.now());
    Prescription savedPrescription = prescriptionRepository.save(prescription);
    String dispensedJson = toDrugsDispensed(savedPrescription.getPrescriptionItems());
    auditLogService.logAttempt(prescription.getPrescriptionId(), prescription.getPatientId(), prescription.getPharmacy().getPharmacyId(),
            requestedJson, dispensedJson, AuditLogStatus.SUCCESS, null);

    return dataMapper.toPrescriptionDto(savedPrescription);
  }

  private String toRequestDrugs(Set<PrescriptionItem> prescriptionItems) {
    StringBuilder sb = new StringBuilder();
    for (PrescriptionItem item : prescriptionItems) {
      sb.append(item.getDrug().getName()).append(": ").append(item.getDrug().getDrugId()).append("\n");
    }
    return sb.toString();
  }

  private String toDrugsDispensed(Set<PrescriptionItem> prescriptionItems) {
    StringBuilder sb = new StringBuilder();
    for (PrescriptionItem item : prescriptionItems) {
      sb.append(item.getDrug().getName()).append(": ").append(item.getDosage()).append("\n");
    }
    return sb.toString();
  }

}