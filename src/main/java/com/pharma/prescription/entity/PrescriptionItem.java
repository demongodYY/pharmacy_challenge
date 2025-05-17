package com.pharma.prescription.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prescription_items")
@NoArgsConstructor
public class PrescriptionItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @EqualsAndHashCode.Include
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "prescription_id", nullable = false)
  @Getter
  private Prescription prescription;

  @Getter
  @EqualsAndHashCode.Include
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "drug_id", nullable = false)
  private Drug drug;

  @Getter
  @Column(nullable = false)
  private Integer dosage;

  public PrescriptionItem(Prescription prescription, Drug drug, Integer dosage) {
    this.prescription = prescription;
    this.drug = drug;
    this.dosage = dosage;
  }
}
