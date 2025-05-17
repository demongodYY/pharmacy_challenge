package com.pharma.prescription.service;

import com.pharma.prescription.dto.DrugDto;
import com.pharma.prescription.dto.DrugRequestDto;
import com.pharma.prescription.entity.Drug;
import com.pharma.prescription.repository.DrugRepository;
import com.pharma.prescription.shared.DataMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class DrugServiceTest {

  @Test
  void createDrug() {
    DrugRepository drugRepository = mock(DrugRepository.class);
    DataMapper dataMapper = mock(DataMapper.class);

    var requestDto = new DrugRequestDto("Aspirin", "Pharma Inc", "B123", LocalDate.of(2025, 12, 31), 100);
    var savedDrug = new Drug("Aspirin", "Pharma Inc", "B123", LocalDate.of(2025, 12, 31), 100);
    var drugDto = new DrugDto(java.util.UUID.randomUUID(), "Aspirin", "Pharma Inc", "B123", LocalDate.of(2025, 12, 31), 100);

    when(drugRepository.save(any(Drug.class))).thenReturn(savedDrug);
    when(dataMapper.toDrugDto(savedDrug)).thenReturn(drugDto);

    DrugService drugService = new DrugService(drugRepository, dataMapper);

    DrugDto result = drugService.create(requestDto);

    assertNotNull(result);
    assertEquals(drugDto, result);
    verify(drugRepository, times(1)).save(any(Drug.class));
    verify(dataMapper, times(1)).toDrugDto(savedDrug);
  }
}