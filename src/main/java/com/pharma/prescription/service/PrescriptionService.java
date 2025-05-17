package com.pharma.prescription.service;

import com.pharma.prescription.dto.PrescriptionDto;
import com.pharma.prescription.dto.PrescriptionItemRequestDto;
import com.pharma.prescription.dto.PrescriptionRequestDto;
import com.pharma.prescription.entity.*;
import com.pharma.prescription.exception.BusinessRuleException;
import com.pharma.prescription.exception.ResourceNotFoundException;
import com.pharma.prescription.repository.*;
import com.pharma.prescription.shared.DataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PrescriptionService {
  private final PrescriptionRepository prescriptionRepository;
  private final PrescriptionItemRepository prescriptionItemRepository;
  private final PharmacyRepository pharmacyRepository;
  private final DrugRepository drugRepository;
  private final PharmacyDrugAllocationRepository allocationRepository;
  private final DataMapper dataMapper;

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
}