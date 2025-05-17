package com.pharma.prescription.controller;

import com.pharma.prescription.dto.AuditLogDto;
import com.pharma.prescription.entity.enumration.AuditLogStatus;
import com.pharma.prescription.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit_logs")
@Tag(name = "AuditLog", description = "Audit Log API")
@RequiredArgsConstructor
@Slf4j
public class AuditLogController {
  final AuditLogService auditLogService;

  @GetMapping
  @Operation(summary = "Get audit logs")
  public ResponseEntity<List<AuditLogDto>> getAuditLogs(
          @RequestParam(required = false) String patientId,
          @RequestParam(required = false) UUID pharmacyId,
          @RequestParam(required = false) AuditLogStatus status) {
    return ResponseEntity.ok().body(auditLogService.getAuditLogs(patientId, pharmacyId, status));
  }
}
