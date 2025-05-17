package com.pharma.prescription.shared;

import com.pharma.prescription.dto.*;
import com.pharma.prescription.entity.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DataMapper {
  public PharmacyDto toPharmacyDto(Pharmacy pharmacy) {
    if (pharmacy == null) return null;

    return new PharmacyDto(
            pharmacy.getPharmacyId(),
            pharmacy.getName(),
            pharmacy.getAddress());
  }

  public DrugDto toDrugDto(Drug savedDrug) {
    if (savedDrug == null) return null;

    return new DrugDto(
            savedDrug.getDrugId(),
            savedDrug.getName(),
            savedDrug.getManufacturer(),
            savedDrug.getBatchNumber(),
            savedDrug.getExpiryDate(),
            savedDrug.getStock()
    );
  }

  public PharmacyDrugAllocationDto toPharmacyDrugAllocationDto(PharmacyDrugAllocation pharmacyDrugAllocation) {
    if (pharmacyDrugAllocation == null) return null;

    return new PharmacyDrugAllocationDto(
            pharmacyDrugAllocation.getPharmacy().getPharmacyId(),
            pharmacyDrugAllocation.getDrug().getDrugId(),
            pharmacyDrugAllocation.isContracted(),
            pharmacyDrugAllocation.getAllocated()
    );
  }

  public PrescriptionDto toPrescriptionDto(Prescription prescription) {
    if (prescription == null) return null;

    return new PrescriptionDto(
            prescription.getPrescriptionId(),
            prescription.getPatientId(),
            toPharmacyDto(prescription.getPharmacy()),
            prescription.getPrescriptionItems().stream()
                    .map(this::toPrescriptionItemDto)
                    .collect(Collectors.toSet()),
            prescription.getStatus(),
            prescription.getPrescriptionDate(),
            prescription.getFulfillmentDate()
    );
  }

  public PrescriptionItemDto toPrescriptionItemDto(PrescriptionItem prescriptionItem) {
    if (prescriptionItem == null) return null;

    return new PrescriptionItemDto(
            prescriptionItem.getDosage(),
            toDrugDto(prescriptionItem.getDrug()),
            prescriptionItem.getPrescription().getPrescriptionId()
    );
  }

  public AuditLogDto toAuditLogDto(AuditLog auditLog) {
    if (auditLog == null) return null;

    return new AuditLogDto(
            auditLog.getLogId(),
            auditLog.getPrescriptionId(),
            auditLog.getPatientId(),
            auditLog.getPharmacyId(),
            auditLog.getDrugsRequested(),
            auditLog.getDrugsDispensed(),
            auditLog.getStatus(),
            auditLog.getFailureReasons(),
            auditLog.getTimestamp()
    );
  }
}
