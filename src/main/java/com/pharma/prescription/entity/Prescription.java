package com.pharma.prescription.entity;

import com.pharma.prescription.entity.enumration.PrescriptionStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "prescriptions")
@NoArgsConstructor
@ToString(exclude = {"prescriptionItems", "pharmacy"})
public class Prescription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "prescription_id", nullable = false, unique = true, updatable = false)
  private UUID prescriptionId;

  @Column(nullable = false) // Can be linked to a Patient entity if patients are managed
  private String patientId; // For simplicity, using String. Could be UUID.

  @EqualsAndHashCode.Include
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pharmacy_id", nullable = false)
  private Pharmacy pharmacy;

  @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private Set<PrescriptionItem> prescriptionItems = new HashSet<>();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PrescriptionStatus status;

  @Column(nullable = false)
  private LocalDateTime prescriptionDate;

  private LocalDateTime fulfillmentDate;

  @Column(columnDefinition = "TEXT")
  private String failureReason; // Store reasons if it fails

  @PrePersist
  protected void onCreate() {
    if (prescriptionId == null) {
      prescriptionId = UUID.randomUUID();
    }
    if (prescriptionDate == null) {
      prescriptionDate = LocalDateTime.now();
    }
    if (status == null) {
      status = PrescriptionStatus.PENDING;
    }
  }
}