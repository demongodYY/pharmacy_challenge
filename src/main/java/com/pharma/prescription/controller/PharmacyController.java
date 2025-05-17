package com.pharma.prescription.controller;

import com.pharma.prescription.dto.PharmacyDto;
import com.pharma.prescription.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacies")
@RequiredArgsConstructor
@Slf4j
public class PharmacyController {

  private final PharmacyService pharmacyService;

  @GetMapping("")
  public ResponseEntity<List<PharmacyDto>> listPharmacies() {
    return ResponseEntity.ok()
        .body(pharmacyService.listAll());
  }
}
