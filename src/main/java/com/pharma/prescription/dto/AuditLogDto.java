package com.pharma.prescription.dto;

import com.pharma.prescription.entity.enumration.AuditLogStatus;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.pharma.prescription.entity.AuditLog}
 */
@Value
public class AuditLogDto implements Serializable {
  UUID logId;
  UUID prescriptionId;
  String patientId;
  UUID pharmacyId;
  String drugsRequested;
  String drugsDispensed;
  AuditLogStatus status;
  String failureReasons;
  LocalDateTime timestamp;
}
