package com.pharma.prescription.controller;


import com.pharma.prescription.dto.DrugDto;
import com.pharma.prescription.dto.DrugRequestDto;
import com.pharma.prescription.service.DrugService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drugs")
@RequiredArgsConstructor
@Slf4j
public class DragController {
  private final DrugService drugService;

  @PostMapping("")
  public ResponseEntity<DrugDto> createDrug(@RequestBody DrugRequestDto drugRequestDto) {
    return ResponseEntity.ok().body(drugService.create(drugRequestDto));
  }
}
