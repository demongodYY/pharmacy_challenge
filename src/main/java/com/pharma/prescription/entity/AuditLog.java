package com.pharma.prescription.entity;

import com.pharma.prescription.entity.enumration.AuditLogStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@NoArgsConstructor
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "log_id", updatable = false, nullable = false, unique = true)
  private UUID logId;

  @Column(nullable = false)
  private UUID prescriptionPublicId; // Link to the prescription's public ID

  @Column(nullable = false)
  private String patientId;

  @Column(nullable = false)
  private UUID pharmacyPublicId;

  @Column(columnDefinition = "TEXT") // Store as JSON string or similar
  private String drugsRequested; // e.g., [{"drugPublicId": "uuid", "quantity": 2, "dosage": "10mg"}]

  @Column(columnDefinition = "TEXT") // Store as JSON string or similar
  private String drugsDispensed; // if successful, same format

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
      this.logId = UUID.randomUUID(); // 生成一个新的随机 UUID
    }
    if (timestamp == null) {
      timestamp = LocalDateTime.now();
    }
  }
}