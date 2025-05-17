package com.pharma.prescription.shared;

import com.pharma.prescription.dto.DrugDto;
import com.pharma.prescription.entity.*;
import com.pharma.prescription.entity.enumration.PrescriptionStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

  @Test
  void toPharmacyDrugAllocationDto_mapsEntityToDtoCorrectly() {
    UUID drugId = UUID.randomUUID();
    UUID pharmacyId = UUID.randomUUID();
    var pharmacy = new Pharmacy(1L, pharmacyId, "Test Pharmacy", "123 Test St", null, null);

    var drug = new Drug(1L, drugId, "Paracetamol", "Pharma Inc", "B456", LocalDate.of(2026, 1, 1), null, null, 200);
    var allocation = new PharmacyDrugAllocation(1L, pharmacy, drug, true, 50);

    DataMapper mapper = new DataMapper();
    var dto = mapper.toPharmacyDrugAllocationDto(allocation);

    assertNotNull(dto);
    assertEquals(pharmacyId, dto.getPharmacyId());
    assertEquals(drugId, dto.getDrug_id());
    assertTrue(dto.isContracted());
    assertEquals(50, dto.getAllocated());
  }

  @Test
  void toPrescriptionDto_mapsEntityToDtoCorrectly() {
    var prescriptionId = UUID.randomUUID();
    var patientId = "test-patient";
    var pharmacy = new Pharmacy(3L, java.util.UUID.randomUUID(), "Pharmacy Name", "Pharmacy Address", null, null);
    var prescriptionItems = Set.of(new PrescriptionItem());
    var status = PrescriptionStatus.PENDING;
    var prescriptionDate = LocalDateTime.now();
    var fulfillmentDate = LocalDateTime.now().plusDays(1);

    var prescription = new Prescription(
            1L,
            prescriptionId,
            patientId,
            pharmacy,
            prescriptionItems,
            status,
            prescriptionDate,
            fulfillmentDate
    );

    DataMapper mapper = new DataMapper();
    var dto = mapper.toPrescriptionDto(prescription);

    assertNotNull(dto);
    assertEquals(prescriptionId, dto.getPrescriptionId());
    assertEquals(patientId, dto.getPatientId());
    assertNotNull(dto.getPharmacy());
    assertEquals(pharmacy.getPharmacyId(), dto.getPharmacy().getPharmacyId());
    assertEquals(prescriptionItems, dto.getPrescriptionItems());
    assertEquals(status, dto.getStatus());
    assertEquals(prescriptionDate, dto.getPrescriptionDate());
    assertEquals(fulfillmentDate, dto.getFulfillmentDate());
  }
}