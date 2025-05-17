package com.pharma.prescription.controller;// DragControllerTest.java

import com.pharma.prescription.dto.DrugDto;
import com.pharma.prescription.dto.DrugRequestDto;
import com.pharma.prescription.dto.PharmacyDragAllocationRequestDto;
import com.pharma.prescription.dto.PharmacyDrugAllocationDto;
import com.pharma.prescription.service.DrugService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DragControllerTest {

  @Test
  void createDrug() {
    DrugService drugService = mock(DrugService.class);
    var uuid = java.util.UUID.randomUUID();
    DrugRequestDto requestDto = new DrugRequestDto("Aspirin", "Pharma Inc.", "B123", LocalDate.of(2025, 12, 31), 100);
    DrugDto responseDto = new DrugDto(uuid, "Aspirin", "Pharma Inc.", "B123", LocalDate.of(2025, 12, 31), 100);
    when(drugService.create(requestDto)).thenReturn(responseDto);

    DragController controller = new DragController(drugService);

    ResponseEntity<DrugDto> response = controller.createDrug(requestDto);

    assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
    assertEquals(responseDto, response.getBody());
    verify(drugService, times(1)).create(requestDto);
  }

  @Test
  void allocateDrugToPharmacy() {
    DrugService drugService = mock(DrugService.class);
    DragController controller = new DragController(drugService);
    UUID drugId = UUID.randomUUID();
    List<PharmacyDragAllocationRequestDto> requestDtos = List.of(
            mock(PharmacyDragAllocationRequestDto.class)
    );
    List<PharmacyDrugAllocationDto> allocationDtos = List.of(
            mock(PharmacyDrugAllocationDto.class)
    );
    when(drugService.allocateDrugToPharmacy(drugId, requestDtos)).thenReturn(allocationDtos);

    ResponseEntity<List<PharmacyDrugAllocationDto>> response = controller.allocateDrugToPharmacy(drugId, requestDtos);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(allocationDtos, response.getBody());
    verify(drugService, times(1)).allocateDrugToPharmacy(drugId, requestDtos);
  }
}