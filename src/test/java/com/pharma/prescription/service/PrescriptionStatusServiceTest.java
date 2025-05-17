package com.pharma.prescription.service;

import com.pharma.prescription.entity.Pharmacy;
import com.pharma.prescription.entity.Prescription;
import com.pharma.prescription.entity.enumration.PrescriptionStatus;
import com.pharma.prescription.repository.PrescriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class PrescriptionStatusServiceTest {

  @Mock
  private PrescriptionRepository prescriptionRepository;

  @InjectMocks
  private PrescriptionStatusService prescriptionStatusService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void updatePrescriptionStatus_shouldUpdateStatusAndSave() {
    var pharmacy = new Pharmacy();
    Prescription prescription = new Prescription(
            "patientId",
            pharmacy
    );
    prescription.setStatus(PrescriptionStatus.PENDING);

    PrescriptionStatus newStatus = PrescriptionStatus.FAILED_FULFILLMENT;

    when(prescriptionRepository.save(prescription)).thenReturn(prescription);

    prescriptionStatusService.updatePrescriptionStatus(prescription, newStatus);

    verify(prescriptionRepository, times(1)).save(prescription);
  }
}