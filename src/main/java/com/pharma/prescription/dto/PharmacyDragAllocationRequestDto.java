package com.pharma.prescription.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class PharmacyDragAllocationRequestDto implements Serializable {
  UUID pharmacyId;
  int allocated;
}
