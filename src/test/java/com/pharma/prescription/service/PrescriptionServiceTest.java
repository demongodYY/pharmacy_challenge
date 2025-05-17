package com.pharma.prescription.service;

import com.pharma.prescription.dto.PrescriptionDto;
import com.pharma.prescription.dto.PrescriptionItemRequestDto;
import com.pharma.prescription.dto.PrescriptionRequestDto;
import com.pharma.prescription.entity.*;
import com.pharma.prescription.exception.BusinessRuleException;
import com.pharma.prescription.exception.ResourceNotFoundException;
import com.pharma.prescription.repository.*;
import com.pharma.prescription.shared.DataMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PrescriptionServiceTest {

  @Mock
  private PrescriptionRepository prescriptionRepository;
  @Mock
  private PrescriptionItemRepository prescriptionItemRepository;
  @Mock
  private PharmacyRepository pharmacyRepository;
  @Mock
  private DrugRepository drugRepository;
  @Mock
  private PharmacyDrugAllocationRepository allocationRepository;
  @Mock
  private DataMapper dataMapper;

  @InjectMocks
  private PrescriptionService prescriptionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void create_shouldReturnPrescriptionDto_whenValidRequest() {
    // Arrange
    var pharmacyId = UUID.randomUUID();
    var patientId = "test-patient-id";
    var drugId = UUID.randomUUID();
    int dosage = 5;

    PrescriptionItemRequestDto itemRequest = mock(PrescriptionItemRequestDto.class);
    when(itemRequest.getDragId()).thenReturn(drugId);
    when(itemRequest.getDosage()).thenReturn(dosage);

    PrescriptionRequestDto requestDto = mock(PrescriptionRequestDto.class);
    when(requestDto.getPharmacyId()).thenReturn(pharmacyId);
    when(requestDto.getPatientId()).thenReturn(patientId);
    when(requestDto.getPrescriptionItems()).thenReturn(List.of(itemRequest));

    Pharmacy pharmacy = mock(Pharmacy.class);
    when(pharmacy.getPharmacyId()).thenReturn(pharmacyId);
    when(pharmacyRepository.findByPharmacyId(pharmacyId)).thenReturn(Optional.of(pharmacy));

    Drug drug = mock(Drug.class);
    when(drug.getDrugId()).thenReturn(drugId);
    when(drug.getExpiryDate()).thenReturn(LocalDate.now().plusDays(10));
    when(drugRepository.findByDrugId(drugId)).thenReturn(Optional.of(drug));

    PharmacyDrugAllocation allocation = mock(PharmacyDrugAllocation.class);
    when(allocation.isContracted()).thenReturn(true);
    when(allocation.getAllocated()).thenReturn(10);
    when(allocationRepository.findByPharmacyPublicIdAndDrugPublicId(pharmacyId, drugId)).thenReturn(Optional.of(allocation));

    Prescription prescription = mock(Prescription.class);
    Prescription savedPrescription = mock(Prescription.class);
    when(prescriptionRepository.save(any(Prescription.class))).thenReturn(savedPrescription);

    PrescriptionDto prescriptionDto = mock(PrescriptionDto.class);
    when(dataMapper.toPrescriptionDto(savedPrescription)).thenReturn(prescriptionDto);

    // Act
    PrescriptionDto result = prescriptionService.create(requestDto);

    // Assert
    assertEquals(prescriptionDto, result);
    verify(prescriptionItemRepository, times(1)).save(any(PrescriptionItem.class));
  }

  @Test
  void create_shouldThrowBusinessRuleException_whenNoItems() {
    PrescriptionRequestDto requestDto = mock(PrescriptionRequestDto.class);
    when(requestDto.getPrescriptionItems()).thenReturn(Collections.emptyList());

    assertThrows(BusinessRuleException.class, () -> prescriptionService.create(requestDto));
  }

  @Test
  void create_shouldThrowResourceNotFoundException_whenPharmacyNotFound() {
    var pharmacyId = UUID.randomUUID();
    PrescriptionRequestDto requestDto = mock(PrescriptionRequestDto.class);
    when(requestDto.getPharmacyId()).thenReturn(pharmacyId);
    when(requestDto.getPrescriptionItems()).thenReturn(List.of(mock(PrescriptionItemRequestDto.class)));
    when(pharmacyRepository.findByPharmacyId(pharmacyId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> prescriptionService.create(requestDto));
  }

  @Test
  void create_shouldThrowResourceNotFoundException_whenDrugNotFound() {
    var drugId = UUID.randomUUID();
    var pharmacyId = UUID.randomUUID();

    PrescriptionItemRequestDto itemRequest = mock(PrescriptionItemRequestDto.class);
    when(itemRequest.getDragId()).thenReturn(drugId);
    when(itemRequest.getDosage()).thenReturn(1);

    PrescriptionRequestDto requestDto = mock(PrescriptionRequestDto.class);
    when(requestDto.getPharmacyId()).thenReturn(pharmacyId);
    when(requestDto.getPrescriptionItems()).thenReturn(List.of(itemRequest));
    Pharmacy pharmacy = mock(Pharmacy.class);
    when(pharmacyRepository.findByPharmacyId(pharmacyId)).thenReturn(Optional.of(pharmacy));
    when(drugRepository.findByDrugId(drugId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> prescriptionService.create(requestDto));
  }

  @Test
  void create_shouldThrowBusinessRuleException_whenDrugExpired() {
    var drugId = UUID.randomUUID();
    var pharmacyId = UUID.randomUUID();
    PrescriptionItemRequestDto itemRequest = mock(PrescriptionItemRequestDto.class);
    when(itemRequest.getDragId()).thenReturn(drugId);
    when(itemRequest.getDosage()).thenReturn(1);

    PrescriptionRequestDto requestDto = mock(PrescriptionRequestDto.class);
    when(requestDto.getPharmacyId()).thenReturn(pharmacyId);
    when(requestDto.getPrescriptionItems()).thenReturn(List.of(itemRequest));
    Pharmacy pharmacy = mock(Pharmacy.class);
    when(pharmacyRepository.findByPharmacyId(pharmacyId)).thenReturn(Optional.of(pharmacy));
    Drug drug = mock(Drug.class);
    when(drug.getExpiryDate()).thenReturn(LocalDate.now().minusDays(1));
    when(drug.getDrugId()).thenReturn(drugId);
    when(drugRepository.findByDrugId(drugId)).thenReturn(Optional.of(drug));

    assertThrows(BusinessRuleException.class, () -> prescriptionService.create(requestDto));
  }

  @Test
  void create_shouldThrowResourceNotFoundException_whenAllocationNotFound() {
    var drugId = UUID.randomUUID();
    var pharmacyId = UUID.randomUUID();
    PrescriptionItemRequestDto itemRequest = mock(PrescriptionItemRequestDto.class);
    when(itemRequest.getDragId()).thenReturn(drugId);
    when(itemRequest.getDosage()).thenReturn(1);

    PrescriptionRequestDto requestDto = mock(PrescriptionRequestDto.class);
    when(requestDto.getPharmacyId()).thenReturn(pharmacyId);
    when(requestDto.getPrescriptionItems()).thenReturn(List.of(itemRequest));
    Pharmacy pharmacy = mock(Pharmacy.class);
    when(pharmacy.getPharmacyId()).thenReturn(pharmacyId);
    when(pharmacyRepository.findByPharmacyId(pharmacyId)).thenReturn(Optional.of(pharmacy));
    Drug drug = mock(Drug.class);
    when(drug.getExpiryDate()).thenReturn(LocalDate.now().plusDays(1));
    when(drug.getDrugId()).thenReturn(drugId);
    when(drugRepository.findByDrugId(pharmacyId)).thenReturn(Optional.of(drug));
    when(allocationRepository.findByPharmacyPublicIdAndDrugPublicId(pharmacyId, drugId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> prescriptionService.create(requestDto));
  }

  @Test
  void create_shouldThrowBusinessRuleException_whenNotContracted() {
    var drugId = UUID.randomUUID();
    var pharmacyId = UUID.randomUUID();
    PrescriptionItemRequestDto itemRequest = mock(PrescriptionItemRequestDto.class);
    when(itemRequest.getDragId()).thenReturn(drugId);
    when(itemRequest.getDosage()).thenReturn(1);

    PrescriptionRequestDto requestDto = mock(PrescriptionRequestDto.class);
    when(requestDto.getPharmacyId()).thenReturn(pharmacyId);
    when(requestDto.getPrescriptionItems()).thenReturn(List.of(itemRequest));
    Pharmacy pharmacy = mock(Pharmacy.class);
    when(pharmacy.getPharmacyId()).thenReturn(pharmacyId);
    when(pharmacyRepository.findByPharmacyId(pharmacyId)).thenReturn(Optional.of(pharmacy));
    Drug drug = mock(Drug.class);
    when(drug.getExpiryDate()).thenReturn(LocalDate.now().plusDays(1));
    when(drug.getDrugId()).thenReturn(drugId);
    when(drugRepository.findByDrugId(drugId)).thenReturn(Optional.of(drug));
    PharmacyDrugAllocation allocation = mock(PharmacyDrugAllocation.class);
    when(allocation.isContracted()).thenReturn(false);
    when(allocationRepository.findByPharmacyPublicIdAndDrugPublicId(pharmacyId, drugId)).thenReturn(Optional.of(allocation));

    assertThrows(BusinessRuleException.class, () -> prescriptionService.create(requestDto));
  }

  @Test
  void create_shouldThrowBusinessRuleException_whenDosageExceedsAllocation() {
    var drugId = UUID.randomUUID();
    var pharmacyId = UUID.randomUUID();
    PrescriptionItemRequestDto itemRequest = mock(PrescriptionItemRequestDto.class);
    when(itemRequest.getDragId()).thenReturn(drugId);
    when(itemRequest.getDosage()).thenReturn(20);

    PrescriptionRequestDto requestDto = mock(PrescriptionRequestDto.class);
    when(requestDto.getPharmacyId()).thenReturn(pharmacyId);
    when(requestDto.getPrescriptionItems()).thenReturn(List.of(itemRequest));
    Pharmacy pharmacy = mock(Pharmacy.class);
    when(pharmacy.getPharmacyId()).thenReturn(pharmacyId);
    when(pharmacyRepository.findByPharmacyId(pharmacyId)).thenReturn(Optional.of(pharmacy));
    Drug drug = mock(Drug.class);
    when(drug.getExpiryDate()).thenReturn(LocalDate.now().plusDays(1));
    when(drug.getDrugId()).thenReturn(drugId);
    when(drugRepository.findByDrugId(drugId)).thenReturn(Optional.of(drug));
    PharmacyDrugAllocation allocation = mock(PharmacyDrugAllocation.class);
    when(allocation.isContracted()).thenReturn(true);
    when(allocation.getAllocated()).thenReturn(10);
    when(allocationRepository.findByPharmacyPublicIdAndDrugPublicId(pharmacyId, drugId)).thenReturn(Optional.of(allocation));

    assertThrows(BusinessRuleException.class, () -> prescriptionService.create(requestDto));
  }
}