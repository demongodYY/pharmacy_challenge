package com.pharma.prescription.service;

import com.pharma.prescription.entity.Prescription;
import com.pharma.prescription.entity.enumration.PrescriptionStatus;
import com.pharma.prescription.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrescriptionStatusService {
  private final PrescriptionRepository prescriptionRepository;

  // In a separate service or the same service
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updatePrescriptionStatus(Prescription prescription, PrescriptionStatus status) {
    prescription.setStatus(status);
    prescriptionRepository.save(prescription);
  }
}
