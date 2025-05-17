package com.pharma.prescription.controller;

import com.pharma.prescription.dto.PharmacyDto;
import com.pharma.prescription.dto.PharmacyRequestDto;
import com.pharma.prescription.service.PharmacyService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PharmacyControllerTest {

  @Test
  void listPharmacies_returnsListOfPharmacies() {
    PharmacyService pharmacyService = mock(PharmacyService.class);
    List<PharmacyDto> mockList =
            Arrays.asList(
                    new PharmacyDto(java.util.UUID.randomUUID(), "123 Main St", "", null, null),
                    new PharmacyDto(java.util.UUID.randomUUID(), "Pharmacy B", "456 Elm St", null, null));
    when(pharmacyService.listAll()).thenReturn(mockList);

    PharmacyController controller = new PharmacyController(pharmacyService);

    ResponseEntity<List<PharmacyDto>> response = controller.listPharmacies();

    assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
    assertEquals(mockList, response.getBody());
    verify(pharmacyService, times(1)).listAll();
  }

  @Test
  void createPharmacy_createsAndReturnsPharmacy() {
    PharmacyService pharmacyService = mock(PharmacyService.class);
    PharmacyRequestDto requestDto = new PharmacyRequestDto("Pharmacy A", "789 Oak St");
    PharmacyDto responseDto = new PharmacyDto(java.util.UUID.randomUUID(), "Pharmacy A", "789 Oak St", null, null);
    when(pharmacyService.create(requestDto)).thenReturn(responseDto);

    PharmacyController controller = new PharmacyController(pharmacyService);

    ResponseEntity<PharmacyDto> response = controller.createPharmacy(requestDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(responseDto, response.getBody());
    verify(pharmacyService, times(1)).create(requestDto);
  }
}
