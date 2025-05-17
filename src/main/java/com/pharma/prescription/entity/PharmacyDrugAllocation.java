package com.pharma.prescription.entity;

import jakarta.persistence.*;
import lombok.*;

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

  @Getter
  @EqualsAndHashCode.Include
  @ManyToOne(optional = false)
  @JoinColumn(name = "pharmacy_id", nullable = false)
  private Pharmacy pharmacy;

  @Getter
  @EqualsAndHashCode.Include
  @ManyToOne(optional = false)
  @JoinColumn(name = "drug_id", nullable = false)
  private Drug drug;

  @Getter
  @Setter
  @Column(nullable = false)
  private boolean contracted;

  @Getter
  @Setter
  @Column(name = "allocated", nullable = false)
  private int allocated;

  public PharmacyDrugAllocation(Drug drug, Pharmacy pharmacy, int allocated) {
    this.pharmacy = pharmacy;
    this.drug = drug;
    this.contracted = true;
    this.allocated = allocated;
  }
}
