package com.pharma.prescription.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.pharma.prescription.dto.PharmacyDto;
import com.pharma.prescription.entity.Pharmacy; // Import the correct entity
import com.pharma.prescription.repository.PharmacyRepository;
import com.pharma.prescription.shared.DataMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class PharmacyServiceTest {

  @Test
  void listAll() {
    PharmacyRepository pharmacyRepository = mock(PharmacyRepository.class);
    DataMapper dataMapper = mock(DataMapper.class);

    Pharmacy pharmacyEntity1 = mock(Pharmacy.class); // Use the actual entity
    Pharmacy pharmacyEntity2 = mock(Pharmacy.class);
    var pharmacyDto1 = new PharmacyDto(java.util.UUID.randomUUID(), "123 Main St", "", null, null);
    var pharmacyDto2 =
        new PharmacyDto(java.util.UUID.randomUUID(), "Pharmacy B", "456 Elm St", null, null);

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
}
