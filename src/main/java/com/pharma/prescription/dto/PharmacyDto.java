package com.pharma.prescription.dto;

import com.pharma.prescription.entity.PharmacyDrugAllocation;
import com.pharma.prescription.entity.Prescription;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import lombok.Value;

/** DTO for {@link com.pharma.prescription.entity.Pharmacy} */
@Value
public class PharmacyDto implements Serializable {
  UUID pharmacyId;
  String name;
  String address;
  Set<PharmacyDrugAllocation> drugAllocations;
  Set<Prescription> prescriptions;
}
