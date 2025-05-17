package com.pharma.prescription.dto;

import com.pharma.prescription.entity.PrescriptionItem;
import com.pharma.prescription.entity.enumration.PrescriptionStatus;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link com.pharma.prescription.entity.Prescription}
 */
@Value
public class PrescriptionDto {
  UUID prescriptionId;
  String patientId;
  PharmacyDto pharmacy;
  Set<PrescriptionItem> prescriptionItems;
  PrescriptionStatus status;
  LocalDateTime prescriptionDate;
  LocalDateTime fulfillmentDate;
}