package com.pharma.prescription.entity;

import com.pharma.prescription.entity.enumration.PrescriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "prescriptions")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"prescriptionItems", "pharmacy"})
public class Prescription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Column(name = "prescription_id", nullable = false, unique = true, updatable = false)
  private UUID prescriptionId;

  @Getter
  @Column(nullable = false) // Can be linked to a Patient entity if patients are managed
  private String patientId; // For simplicity, using String. Could be UUID.

  @Getter
  @EqualsAndHashCode.Include
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pharmacy_id", nullable = false)
  private Pharmacy pharmacy;

  @Getter
  @OneToMany(mappedBy = "prescription", fetch = FetchType.LAZY)
  private Set<PrescriptionItem> prescriptionItems = new HashSet<>();

  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PrescriptionStatus status;

  @Getter
  @Column(nullable = false)
  private LocalDateTime prescriptionDate;

  @Getter
  @Setter
  private LocalDateTime fulfillmentDate;

  @Version
  private Long version;

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

  public Prescription(String patientId, Pharmacy pharmacy) {
    this.patientId = patientId;
    this.pharmacy = pharmacy;
  }
}
