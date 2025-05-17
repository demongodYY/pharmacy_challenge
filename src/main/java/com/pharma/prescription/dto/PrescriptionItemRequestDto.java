package com.pharma.prescription.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.pharma.prescription.entity.PrescriptionItem}
 */
@Value
public class PrescriptionItemRequestDto implements Serializable {
  UUID dragId;
  Integer dosage;
}