package com.pharma.prescription.repository;

import com.pharma.prescription.entity.AuditLog;
import com.pharma.prescription.entity.enumration.AuditLogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
  // Custom query methods can be defined here if needed
  // For example, to find logs by user or action
  List<AuditLog> findByPatientId(String patientId);

  List<AuditLog> findByPharmacyId(UUID pharmacyId);

  List<AuditLog> findByStatus(AuditLogStatus status);
}
