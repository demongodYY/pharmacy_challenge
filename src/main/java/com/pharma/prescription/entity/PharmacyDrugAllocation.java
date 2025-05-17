package com.pharma.prescription.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "pharmacy_drug_allocations",
    uniqueConstraints = @UniqueConstraint(columnNames = {"pharmacy_id", "drug_id"}))
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyDrugAllocation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @EqualsAndHashCode.Include
  @ManyToOne(optional = false)
  @JoinColumn(name = "pharmacy_id", nullable = false)
  private Pharmacy pharmacy;

  @EqualsAndHashCode.Include
  @ManyToOne(optional = false)
  @JoinColumn(name = "drug_id", nullable = false)
  private Drug drug;

  @Column(nullable = false)
  private boolean contracted;

  @Column(name = "allocated", nullable = false)
  private int allocated;
}
