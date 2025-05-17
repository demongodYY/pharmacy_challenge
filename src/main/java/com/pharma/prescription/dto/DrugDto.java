package com.pharma.prescription.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link com.pharma.prescription.entity.Drug}
 */
@Value
public class DrugDto implements Serializable {
  UUID drugId;
  String name;
  String manufacturer;
  String batchNumber;
  LocalDate expiryDate;
  int stock;
}