package com.pharma.prescription.service;

import com.pharma.prescription.dto.DrugDto;
import com.pharma.prescription.dto.DrugRequestDto;
import com.pharma.prescription.dto.PharmacyDragAllocationRequestDto;
import com.pharma.prescription.dto.PharmacyDrugAllocationDto;
import com.pharma.prescription.entity.Drug;
import com.pharma.prescription.entity.PharmacyDrugAllocation;
import com.pharma.prescription.repository.DrugRepository;
import com.pharma.prescription.repository.PharmacyDrugAllocationRepository;
import com.pharma.prescription.repository.PharmacyRepository;
import com.pharma.prescription.shared.DataMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class DrugServiceTest {

  @Test
  void createDrug() {
    DrugRepository drugRepository = mock(DrugRepository.class);
    DataMapper dataMapper = mock(DataMapper.class);
    PharmacyRepository pharmacyRepository = mock(PharmacyRepository.class);
    PharmacyDrugAllocationRepository pharmacyDrugAllocationRepository = mock(PharmacyDrugAllocationRepository.class);


    var requestDto = new DrugRequestDto("Aspirin", "Pharma Inc", "B123", LocalDate.of(2025, 12, 31), 100);
    var savedDrug = new Drug("Aspirin", "Pharma Inc", "B123", LocalDate.of(2025, 12, 31), 100);
    var drugDto = new DrugDto(java.util.UUID.randomUUID(), "Aspirin", "Pharma Inc", "B123", LocalDate.of(2025, 12, 31), 100);

    when(drugRepository.save(any(Drug.class))).thenReturn(savedDrug);
    when(dataMapper.toDrugDto(savedDrug)).thenReturn(drugDto);

    DrugService drugService = new DrugService(drugRepository, dataMapper, pharmacyRepository, pharmacyDrugAllocationRepository);

    DrugDto result = drugService.create(requestDto);

    assertNotNull(result);
    assertEquals(drugDto, result);
    verify(drugRepository, times(1)).save(any(Drug.class));
    verify(dataMapper, times(1)).toDrugDto(savedDrug);
  }
  // DrugServiceTest.java

  @Test
  void allocateDrugToPharmacy_successfulAllocation() {
    DrugRepository drugRepository = mock(DrugRepository.class);
    DataMapper dataMapper = mock(DataMapper.class);
    PharmacyRepository pharmacyRepository = mock(PharmacyRepository.class);
    PharmacyDrugAllocationRepository pharmacyDrugAllocationRepository = mock(PharmacyDrugAllocationRepository.class);

    DrugService drugService = new DrugService(drugRepository, dataMapper, pharmacyRepository, pharmacyDrugAllocationRepository);

    UUID drugId = UUID.randomUUID();
    UUID pharmacyId = UUID.randomUUID();

    Drug drug = new Drug(1L, drugId, "Paracetamol", "Pharma Inc", "B456", LocalDate.of(2026, 1, 1), null, null, 200);

    PharmacyDragAllocationRequestDto allocationRequest = new PharmacyDragAllocationRequestDto(pharmacyId, 50);
    List<PharmacyDragAllocationRequestDto> allocationRequests = List.of(allocationRequest);

    when(drugRepository.findByDrugId(drugId)).thenReturn(java.util.Optional.of(drug));

    var pharmacy = mock(com.pharma.prescription.entity.Pharmacy.class);
    when(pharmacy.getPharmacyId()).thenReturn(pharmacyId);
    when(pharmacyRepository.findByPharmacyId(pharmacyId)).thenReturn(java.util.Optional.of(pharmacy));
    when(pharmacyDrugAllocationRepository.findByPharmacyPublicIdAndDrugPublicId(pharmacyId, drugId)).thenReturn(java.util.Optional.empty());

    PharmacyDrugAllocation savedAllocation = mock(PharmacyDrugAllocation.class);
    when(pharmacyDrugAllocationRepository.save(any(PharmacyDrugAllocation.class))).thenReturn(savedAllocation);

    PharmacyDrugAllocationDto allocationDto = mock(PharmacyDrugAllocationDto.class);
    when(dataMapper.toPharmacyDrugAllocationDto(savedAllocation)).thenReturn(allocationDto);

    List<PharmacyDrugAllocationDto> result = drugService.allocateDrugToPharmacy(drugId, allocationRequests);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(allocationDto, result.get(0));
    verify(drugRepository).findByDrugId(drugId);
    verify(pharmacyRepository).findByPharmacyId(pharmacyId);
    verify(pharmacyDrugAllocationRepository).save(any(PharmacyDrugAllocation.class));
    verify(dataMapper).toPharmacyDrugAllocationDto(savedAllocation);
  }
}