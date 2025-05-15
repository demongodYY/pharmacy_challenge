package com.pharma.prescription.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyDrugAllocationDTO {
  private Long id;
  private Long pharmacyId;
  private Long drugId;
  private Integer allocated; // Total amount of this drug allocated to the pharmacy
}