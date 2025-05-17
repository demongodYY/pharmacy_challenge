package com.pharma.prescription.entity;

import com.pharma.prescription.entity.enumration.AuditLogStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "audit_logs")
@RequiredArgsConstructor

public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "log_id", updatable = false, nullable = false, unique = true)
  private UUID logId;

  @Column(name = "prescription_public_id", nullable = false)
  private UUID prescriptionId; // Link to the prescription's public ID

  @Column(nullable = false)
  private String patientId;

  @Column(name = "pharmacy_public_id", nullable = false)
  private UUID pharmacyId;

  @Column(columnDefinition = "TEXT")
  private String drugsRequested;

  @Column(columnDefinition = "TEXT")
  private String drugsDispensed;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AuditLogStatus status;

  @Column(columnDefinition = "TEXT")
  private String failureReasons;

  @Column(nullable = false)
  private LocalDateTime timestamp;

  @PrePersist
  protected void onCreate() {
    if (this.logId == null) {
      this.logId = UUID.randomUUID();
    }
    if (timestamp == null) {
      timestamp = LocalDateTime.now();
    }
  }

  public AuditLog(UUID prescriptionId, String patientId, UUID pharmacyId,
                  String drugsRequested, String drugsDispensed, AuditLogStatus status,
                  String failureReasons) {
    this.prescriptionId = prescriptionId;
    this.patientId = patientId;
    this.pharmacyId = pharmacyId;
    this.drugsRequested = drugsRequested;
    this.drugsDispensed = drugsDispensed;
    this.status = status;
    this.failureReasons = failureReasons;
  }
}
