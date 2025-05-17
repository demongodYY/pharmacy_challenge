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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
      throw new BusinessRuleException("Prescription must contain at least one item");
    }

    Pharmacy pharmacy = pharmacyRepository.findByPharmacyId(prescriptionRequestDto.getPharmacyId())
            .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found: " + prescriptionRequestDto.getPharmacyId()));
    Prescription prescription = new Prescription(prescriptionRequestDto.getPatientId(), pharmacy);
    Prescription savedPrescription = prescriptionRepository.save(prescription);

    for (PrescriptionItemRequestDto itemRequest : prescriptionRequestDto.getPrescriptionItems()) {
      Drug drug = drugRepository.findByDrugId(itemRequest.getDragId())
              .orElseThrow(() -> new ResourceNotFoundException("Drug not found: " + itemRequest.getDragId()));

      // 1. Check Drug Expiry
      if (drug.getExpiryDate().isBefore(LocalDate.now())) {
        throw new BusinessRuleException("Drug is expired: " + drug.getDrugId());
      }

      // 2. Check Pharmacy Contract and Allocation
      PharmacyDrugAllocation allocation = allocationRepository
              .findByPharmacyPublicIdAndDrugPublicId(pharmacy.getPharmacyId(), drug.getDrugId())
              .orElseThrow(() -> new ResourceNotFoundException(
                      "Drug allocation is not allocated for this pharmacy: " + itemRequest.getDragId()));
      if (!allocation.isContracted()) {
        throw new BusinessRuleException("Pharmacy is not contracted for this drug: " + drug.getDrugId());
      }
      if (itemRequest.getDosage() > allocation.getAllocated()) {
        throw new BusinessRuleException("Dosage exceeds allocated amount: " + itemRequest.getDosage());
      }

      PrescriptionItem pItem = new PrescriptionItem(savedPrescription, drug, itemRequest.getDosage());
      prescriptionItemRepository.save(pItem);
    }
    return dataMapper.toPrescriptionDto(savedPrescription);
  }

  @Transactional
  public PrescriptionDto fulfillPrescription(UUID prescriptionId) {
    Prescription prescription = prescriptionRepository.findByPrescriptionId(prescriptionId)
            .orElseThrow(() -> new ResourceNotFoundException("Prescription not found: " + prescriptionId));

    if (prescription.getStatus() != PrescriptionStatus.PENDING) {
      throw new BusinessRuleException("Prescription is not in a state that can be fulfilled: " + prescription.getStatus());
    }

    StringBuilder fulfillmentFailures = new StringBuilder();
    List<Drug> drugsToUpdate = new ArrayList<>();
    String requestedJson = toRequestDrugs(prescription.getPrescriptionItems());

    for (PrescriptionItem item : prescription.getPrescriptionItems()) {
      Drug drug = item.getDrug();
      if (item.getDosage() > drug.getStock()) {
        fulfillmentFailures.append("Insufficient stock for drug ")
                .append(drug.getName()).append(" during fulfillment. Requested: ")
                .append(item.getDosage()).append(", Available: ")
                .append(drug.getStock()).append(". ");
        prescriptionStatusService.updatePrescriptionStatus(prescription, PrescriptionStatus.FAILED_FULFILLMENT);
        auditLogService.logAttempt(prescription.getPrescriptionId(), prescription.getPatientId(), prescription.getPharmacy().getPharmacyId(),
                requestedJson, null, AuditLogStatus.FAILURE, fulfillmentFailures.toString());
        throw new BusinessRuleException("Prescription fulfillment failed: " + fulfillmentFailures);
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
              requestedJson, null, AuditLogStatus.FAILURE, "Concurrent stock modification detected.");
      throw new BusinessRuleException("Fulfillment failed due to concurrent drug stock update.", e);
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