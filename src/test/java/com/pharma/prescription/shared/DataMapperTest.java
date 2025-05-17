package com.pharma.prescription.shared;

import com.pharma.prescription.dto.DrugDto;
import com.pharma.prescription.entity.Drug;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataMapperTest {

  @Test
  void toPharmacyDto_mapsEntityToDtoCorrectly() {
    var name = "Test Pharmacy";
    var address = "123 Test St";

    var pharmacy = new com.pharma.prescription.entity.Pharmacy();
    pharmacy.setName(name);
    pharmacy.setAddress(address);

    DataMapper mapper = new DataMapper();
    var dto = mapper.toPharmacyDto(pharmacy);

    assertNotNull(dto);
    assertEquals(name, dto.getName());
    assertEquals(address, dto.getAddress());
  }

  @Test
  void toDrugDto_mapsEntityToDtoCorrectly() {
    var name = "Aspirin";
    var manufacturer = "Pharma Inc.";
    var batchNumber = "B123";
    var expiryDate = LocalDate.of(2025, 12, 31);
    var stock = 100;

    Drug drug = new Drug(name, manufacturer, batchNumber, expiryDate, stock);

    drug.setStock(stock);

    DataMapper mapper = new DataMapper();
    DrugDto dto = mapper.toDrugDto(drug);

    assertNotNull(dto);
    assertEquals(name, dto.getName());
    assertEquals(manufacturer, dto.getManufacturer());
    assertEquals(batchNumber, dto.getBatchNumber());
    assertEquals(expiryDate, dto.getExpiryDate());
    assertEquals(stock, dto.getStock());
  }
}