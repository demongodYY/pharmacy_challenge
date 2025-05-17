package com.pharma.prescription.shared;


import com.pharma.prescription.dto.*;
import com.pharma.prescription.entity.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class DataMapper {
  public PharmacyDto toPharmacyDto(Pharmacy pharmacy) {
    if (pharmacy == null) return null;

    return new PharmacyDto(
            pharmacy.getPharmacyId(),
            pharmacy.getName(),
            pharmacy.getAddress(),
            pharmacy.getDrugAllocations(),
            pharmacy.getPrescriptions()
           );
  }



}