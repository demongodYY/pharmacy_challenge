package com.pharma.prescription.service;

import com.pharma.prescription.dto.AuditLogDto;
import com.pharma.prescription.entity.AuditLog;
import com.pharma.prescription.entity.enumration.AuditLogStatus;
import com.pharma.prescription.repository.AuditLogRepository;
import com.pharma.prescription.shared.DataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService {

  private final AuditLogRepository auditLogRepository;
  private final DataMapper dataMapper;

  @Transactional(propagation = Propagation.REQUIRES_NEW) // Ensure audit log is saved even if main transaction fails
  public void logAttempt(UUID prescriptionPublicId, String patientId, UUID pharmacyPublicId,
                         String drugsRequestedJson, String drugsDispensedJson,
                         AuditLogStatus status, String failureReasons) {
    AuditLog logEntry = new AuditLog(prescriptionPublicId, patientId, pharmacyPublicId,
            drugsRequestedJson, drugsDispensedJson, status, failureReasons);
    auditLogRepository.save(logEntry);
  }

  @Transactional(readOnly = true)
  public List<AuditLogDto> getAuditLogs(String patientId, UUID pharmacyId, AuditLogStatus status) {
    
    return auditLogRepository.findAll().stream()
            .filter(auditLog -> {
              boolean matches = true;
              if (patientId != null) {
                matches &= auditLog.getPatientId().equals(patientId);
              }
              if (pharmacyId != null) {
                matches &= auditLog.getPharmacyId().equals(pharmacyId);
              }
              if (status != null) {
                matches &= auditLog.getStatus() == status;
              }
              return matches;
            })
            .map(dataMapper::toAuditLogDto)
            .collect(Collectors.toList());
  }
}
