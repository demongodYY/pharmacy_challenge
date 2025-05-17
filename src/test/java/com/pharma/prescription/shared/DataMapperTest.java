package com.pharma.prescription.shared;

import com.pharma.prescription.entity.PharmacyDrugAllocation;
import com.pharma.prescription.entity.Prescription;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataMapperTest {

  @Test
  void toPharmacyDto_mapsEntityToDtoCorrectly() {
    var pharmacyId = java.util.UUID.randomUUID();
    var name = "Test Pharmacy";
    var address = "123 Test St";
    Set<PharmacyDrugAllocation> drugAllocations = Collections.emptySet();
    Set<Prescription> prescriptions = Collections.emptySet();

    var pharmacy = new com.pharma.prescription.entity.Pharmacy();
    pharmacy.setPharmacyId(pharmacyId);
    pharmacy.setName(name);
    pharmacy.setAddress(address);
    pharmacy.setDrugAllocations(drugAllocations);
    pharmacy.setPrescriptions(prescriptions);

    DataMapper mapper = new DataMapper();
    var dto = mapper.toPharmacyDto(pharmacy);

    assertNotNull(dto);
    assertEquals(pharmacyId, dto.getPharmacyId());
    assertEquals(name, dto.getName());
    assertEquals(address, dto.getAddress());
    assertEquals(drugAllocations, dto.getDrugAllocations());
    assertEquals(prescriptions, dto.getPrescriptions());
  }
}