package com.pharma.prescription.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.pharma.prescription.entity.Pharmacy}
 *
 * <p>Used for creating or update a new pharmacy.
 */
@Value
public class PharmacyRequestDto implements Serializable {
  String name;
  String address;
}
