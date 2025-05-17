package com.pharma.prescription.shared;

import com.pharma.prescription.dto.DrugDto;
import com.pharma.prescription.dto.PharmacyDto;
import com.pharma.prescription.entity.Drug;
import com.pharma.prescription.entity.Pharmacy;
import org.springframework.stereotype.Component;

@Component
public class DataMapper {
  public PharmacyDto toPharmacyDto(Pharmacy pharmacy) {
    if (pharmacy == null) return null;

    return new PharmacyDto(
            pharmacy.getPharmacyId(),
            pharmacy.getName(),
            pharmacy.getAddress(),
            pharmacy.getDrugAllocations(),
            pharmacy.getPrescriptions());
  }

  public DrugDto toDrugDto(Drug savedDrug) {
    if (savedDrug == null) return null;

    return new DrugDto(
            savedDrug.getDrugId(),
            savedDrug.getName(),
            savedDrug.getManufacturer(),
            savedDrug.getBatchNumber(),
            savedDrug.getExpiryDate(),
            savedDrug.getStock()
    );
  }
}
