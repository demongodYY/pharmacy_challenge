package com.pharma.prescription.repository;

import com.pharma.prescription.entity.Prescription;
import com.pharma.prescription.entity.enumration.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
  Optional<Prescription> findByPatientId(String patientId);

  List<Prescription> findByStatus(PrescriptionStatus status);

  Optional<Prescription> findByPrescriptionId(UUID prescriptionId);
  
}
