package com.pharma.prescription.controller;

import com.pharma.prescription.dto.PharmacyDto;
import com.pharma.prescription.dto.PharmacyRequestDto;
import com.pharma.prescription.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacies")
@Tag(name = "Pharmacies", description = "Pharmacies API")
@RequiredArgsConstructor
@Slf4j
public class PharmacyController {

  private final PharmacyService pharmacyService;

  @GetMapping("")
  @Operation(summary = "List all pharmacies")
  @ApiResponse(responseCode = "200",
          description = "Pharmacies listed successfully",
          content = @Content(mediaType = "application/json",
                  array = @io.swagger.v3.oas.annotations.media.ArraySchema(
                          schema = @Schema(implementation = PharmacyDto.class))))
  public ResponseEntity<List<PharmacyDto>> listPharmacies() {
    return ResponseEntity.ok().body(pharmacyService.listAll());
  }

  @PostMapping("")
  @Operation(summary = "Create a new pharmacy")
  @ApiResponse(responseCode = "200",
          description = "Pharmacy created successfully",
          content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PharmacyDto.class)))
  public ResponseEntity<PharmacyDto> createPharmacy(@RequestBody PharmacyRequestDto pharmacyRequestDto) {
    return ResponseEntity.ok().body(pharmacyService.create(pharmacyRequestDto));
  }
}
