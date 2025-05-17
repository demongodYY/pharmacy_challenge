package com.pharma.prescription.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.pharma.prescription.entity.Pharmacy}
 */
@Value
public class PharmacyDto implements Serializable {
  UUID pharmacyId;
  String name;
  String address;
}
