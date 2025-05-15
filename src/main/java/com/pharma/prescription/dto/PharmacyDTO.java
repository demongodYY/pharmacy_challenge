package com.pharma.prescription.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyDTO {
  private Long id;

  private String name;

  private String address;

  private List<PharmacyDrugAllocationDTO> drugAllocations;
}