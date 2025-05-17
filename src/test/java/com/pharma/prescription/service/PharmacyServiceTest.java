package com.pharma.prescription.service;

import com.pharma.prescription.dto.PharmacyDto;
import com.pharma.prescription.dto.PharmacyRequestDto;
import com.pharma.prescription.entity.Pharmacy;
import com.pharma.prescription.repository.PharmacyRepository;
import com.pharma.prescription.shared.DataMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PharmacyServiceTest {

  @Test
  void listAll() {
    PharmacyRepository pharmacyRepository = mock(PharmacyRepository.class);
    DataMapper dataMapper = mock(DataMapper.class);

    Pharmacy pharmacyEntity1 = mock(Pharmacy.class); // Use the actual entity
    Pharmacy pharmacyEntity2 = mock(Pharmacy.class);
    var pharmacyDto1 = new PharmacyDto(java.util.UUID.randomUUID(), "323 Main St", "");
    var pharmacyDto2 =
            new PharmacyDto(java.util.UUID.randomUUID(), "Pharmacy B", "456 Elm St");

    when(pharmacyRepository.findAll()).thenReturn(Arrays.asList(pharmacyEntity1, pharmacyEntity2));
    when(dataMapper.toPharmacyDto(pharmacyEntity1)).thenReturn(pharmacyDto1);
    when(dataMapper.toPharmacyDto(pharmacyEntity2)).thenReturn(pharmacyDto2);

    PharmacyService pharmacyService = new PharmacyService(pharmacyRepository, dataMapper);

    List<PharmacyDto> result = pharmacyService.listAll();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(pharmacyDto1, result.get(0));
    assertEquals(pharmacyDto2, result.get(1));

    verify(pharmacyRepository, times(1)).findAll();
    verify(dataMapper, times(1)).toPharmacyDto(pharmacyEntity1);
    verify(dataMapper, times(1)).toPharmacyDto(pharmacyEntity2);
  }

  @Test
  void create_savesPharmacyAndReturnsDto() {
    PharmacyRepository pharmacyRepository = mock(PharmacyRepository.class);
    DataMapper dataMapper = mock(DataMapper.class);

    var requestDto = new PharmacyRequestDto("Pharmacy X", "789 Oak Ave");
    var savedPharmacy = new Pharmacy("Pharmacy X", "789 Oak Ave");
    var pharmacyDto = new PharmacyDto(java.util.UUID.randomUUID(), "Pharmacy X", "789 Oak Ave");

    when(pharmacyRepository.save(any(Pharmacy.class))).thenReturn(savedPharmacy);
    when(dataMapper.toPharmacyDto(savedPharmacy)).thenReturn(pharmacyDto);

    PharmacyService pharmacyService = new PharmacyService(pharmacyRepository, dataMapper);

    PharmacyDto result = pharmacyService.create(requestDto);

    assertNotNull(result);
    assertEquals(pharmacyDto, result);
    verify(pharmacyRepository, times(1)).save(any(Pharmacy.class));
    verify(dataMapper, times(1)).toPharmacyDto(savedPharmacy);
  }
}
