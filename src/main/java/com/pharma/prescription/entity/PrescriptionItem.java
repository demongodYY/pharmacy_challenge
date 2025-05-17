package com.pharma.prescription.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
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
  private Prescription prescription;

  @EqualsAndHashCode.Include
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "drug_id", nullable = false)
  private Drug drug;

  @Column(nullable = false)
  private String dosage; // e.g., "1 tablet", "10mg"

  @Column(nullable = false)
  private Integer requestedQuantity;

  private Integer dispensedQuantity; // Will be set upon successful fulfillment
}