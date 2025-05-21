package com.pharma.prescription.controller;

import com.pharma.prescription.dto.PrescriptionDto;
import com.pharma.prescription.dto.PrescriptionRequestDto;
import com.pharma.prescription.exception.IErrorResponse;
import com.pharma.prescription.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @Operation(summary = "Submit a new prescription")
  @ApiResponse(responseCode = "200",
          description = "Prescription allocated successfully",
          content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PrescriptionDto.class)))
  @ApiResponse(responseCode = "400",
          description = "Prescription submitting is invalid",
          content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = IErrorResponse.class)))
  public ResponseEntity<PrescriptionDto> createPrescription(
          @RequestBody final PrescriptionRequestDto prescriptionRequestDto) {
    return ResponseEntity.ok().body(prescriptionService.create(prescriptionRequestDto));
  }

  @PostMapping("/{prescriptionId}/fulfill")
  @Operation(summary = "Fulfill a prescription")
  @ApiResponse(responseCode = "200",
          description = "Prescription fulfilment successfully",
          content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PrescriptionDto.class)))
  @ApiResponse(responseCode = "400",
          description = "Prescription fulfilment is failed",
          content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = IErrorResponse.class)))
  public ResponseEntity<PrescriptionDto> fulfillPrescription(@PathVariable UUID prescriptionId) {
    return ResponseEntity.ok().body(prescriptionService.fulfillPrescription(prescriptionId));
  }
}
