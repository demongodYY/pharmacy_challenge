package com.pharma.prescription.controller;

import com.pharma.prescription.dto.PrescriptionDto;
import com.pharma.prescription.dto.PrescriptionRequestDto;
import com.pharma.prescription.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prescriptions")
@Tag(name = "Prescriptions", description = "Prescriptions API")
@RequiredArgsConstructor
@Slf4j
public class PrescriptionController {
  private final PrescriptionService prescriptionService;

  @PostMapping("")
  @Operation(summary = "Create a new prescription")
  @ApiResponse(responseCode = "200",
          description = "Prescription created successfully",
          content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PrescriptionDto.class)))
  public ResponseEntity<PrescriptionDto> createPrescription(@RequestBody PrescriptionRequestDto prescriptionRequestDto) {
    return ResponseEntity.ok().body(prescriptionService.create(prescriptionRequestDto));
  }
}
