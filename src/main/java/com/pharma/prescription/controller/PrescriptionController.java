package com.pharma.prescription.controller;

import com.pharma.prescription.dto.PrescriptionDto;
import com.pharma.prescription.dto.PrescriptionRequestDto;
import com.pharma.prescription.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/prescriptions")
@Tag(name = "Prescriptions", description = "Prescriptions API")
@RequiredArgsConstructor
@Slf4j
public class PrescriptionController {
  private final PrescriptionService prescriptionService;

  @PostMapping
  @Operation(summary = "Create a new prescription")
  public ResponseEntity<PrescriptionDto> createPrescription(
          @RequestBody PrescriptionRequestDto prescriptionRequestDto) {
    return ResponseEntity.ok().body(prescriptionService.create(prescriptionRequestDto));
  }

  @PostMapping("/{prescriptionId}/fulfill")
  @Operation(summary = "Fulfill a prescription")
  public ResponseEntity<PrescriptionDto> fulfillPrescription(@PathVariable UUID prescriptionId) {
    return ResponseEntity.ok().body(prescriptionService.fulfillPrescription(prescriptionId));
  }
}
