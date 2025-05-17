package com.pharma.prescription.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.pharma.prescription.entity.PharmacyDrugAllocation}
 */
@Value
public class PharmacyDrugAllocationDto implements Serializable {
  UUID pharmacyId;
  UUID drug_id;
  boolean contracted;
  int allocated;
}