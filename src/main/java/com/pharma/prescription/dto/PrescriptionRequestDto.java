package com.pharma.prescription.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.pharma.prescription.entity.Prescription}
 * Used for creating a new prescription
 */

@Value
public class PrescriptionRequestDto implements Serializable {
  UUID pharmacyId;
  String patientId;
  List<PrescriptionItemRequestDto> prescriptionItems;
}