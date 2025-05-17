package com.pharma.prescription.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.pharma.prescription.entity.Drug}
 * <p>Used for creating or update a drag.
 */
@Value
public class DrugRequestDto implements Serializable {
  String name;
  String manufacturer;
  String batchNumber;
  LocalDate expiryDate;
  int stock;
}