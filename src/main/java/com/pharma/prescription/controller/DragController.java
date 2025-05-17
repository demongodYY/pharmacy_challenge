package com.pharma.prescription.controller;


import com.pharma.prescription.dto.DrugDto;
import com.pharma.prescription.dto.DrugRequestDto;
import com.pharma.prescription.dto.PharmacyDragAllocationRequestDto;
import com.pharma.prescription.dto.PharmacyDrugAllocationDto;
import com.pharma.prescription.service.DrugService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/drugs")
@Tag(name = "Drugs", description = "Drugs API")
@RequiredArgsConstructor
@Slf4j
public class DragController {
  private final DrugService drugService;

  @PostMapping("/{id}/allocation")
  @Operation(summary = "Allocate drug to pharmacies")
  @ApiResponse(responseCode = "200",
          description = "Drug allocated successfully",
          content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PharmacyDrugAllocationDto.class)))
  public ResponseEntity<List<PharmacyDrugAllocationDto>> allocateDrugToPharmacy(
          @PathVariable("id") UUID drugId,
          @RequestBody List<PharmacyDragAllocationRequestDto> pharmacyDragAllocationRequestDtos) {
    return ResponseEntity.ok().body(drugService.allocateDrugToPharmacy(drugId, pharmacyDragAllocationRequestDtos));
  }

  @PostMapping("")
  @Operation(summary = "Create a new drug")
  @ApiResponse(responseCode = "200",
          description = "Drug created successfully",
          content = @Content(mediaType = "application/json",
                  array = @io.swagger.v3.oas.annotations.media.ArraySchema(
                          schema = @Schema(implementation = DrugDto.class))))
  public ResponseEntity<DrugDto> createDrug(@RequestBody DrugRequestDto drugRequestDto) {
    return ResponseEntity.ok().body(drugService.create(drugRequestDto));
  }

}
