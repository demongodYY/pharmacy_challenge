package com.pharma.prescription.controller;

import com.pharma.prescription.dto.PrescriptionDto;
import com.pharma.prescription.dto.PrescriptionItemRequestDto;
import com.pharma.prescription.dto.PrescriptionRequestDto;
import com.pharma.prescription.entity.enumration.PrescriptionStatus;
import com.pharma.prescription.service.PrescriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PrescriptionControllerTest {

  @Test
  void createPrescription() {
    var prescriptionId = UUID.randomUUID();
    var pharmacyId = UUID.randomUUID();
    var dragId = UUID.randomUUID();
    List<PrescriptionItemRequestDto> prescriptionItems = List.of(new PrescriptionItemRequestDto(dragId, 1));

    PrescriptionService prescriptionService = mock(PrescriptionService.class);
    PrescriptionController controller = new PrescriptionController(prescriptionService);
    PrescriptionRequestDto requestDto = new PrescriptionRequestDto(pharmacyId, "test-patient-id", prescriptionItems);
    PrescriptionDto responseDto = new PrescriptionDto(prescriptionId, "test-patient-id", null, null, PrescriptionStatus.PENDING, null, null);
    when(prescriptionService.create(requestDto)).thenReturn(responseDto);

    ResponseEntity<PrescriptionDto> response = controller.createPrescription(requestDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(responseDto, response.getBody());
    verify(prescriptionService, times(1)).create(requestDto);
  }
}