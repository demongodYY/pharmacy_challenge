package com.pharma.prescription.shared;

import com.pharma.prescription.dto.DrugDto;
import com.pharma.prescription.dto.PharmacyDrugAllocationDto;
import com.pharma.prescription.dto.PharmacyDto;
import com.pharma.prescription.entity.Drug;
import com.pharma.prescription.entity.Pharmacy;
import com.pharma.prescription.entity.PharmacyDrugAllocation;
import org.springframework.stereotype.Component;

@Component
public class DataMapper {
  public PharmacyDto toPharmacyDto(Pharmacy pharmacy) {
    if (pharmacy == null) return null;

    return new PharmacyDto(
            pharmacy.getPharmacyId(),
            pharmacy.getName(),
            pharmacy.getAddress());
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

  public PharmacyDrugAllocationDto toPharmacyDrugAllocationDto(PharmacyDrugAllocation pharmacyDrugAllocation) {
    if (pharmacyDrugAllocation == null) return null;

    return new PharmacyDrugAllocationDto(
            pharmacyDrugAllocation.getPharmacy().getPharmacyId(),
            pharmacyDrugAllocation.getDrug().getDrugId(),
            pharmacyDrugAllocation.isContracted(),
            pharmacyDrugAllocation.getAllocated()
    );
  }
}
