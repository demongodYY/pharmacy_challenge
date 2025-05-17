package com.pharma.prescription.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.pharma.prescription.entity.PrescriptionItem}
 */
@Value
public class PrescriptionItemDto implements Serializable {
  Integer dosage;
  DrugDto drug;
  UUID prescriptionId;
}