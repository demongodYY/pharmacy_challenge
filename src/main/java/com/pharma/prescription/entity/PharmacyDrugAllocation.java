package com.pharma.prescription.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pharmacy_drug_allocations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"pharmacy_id", "drug_id"})) // Ensures a drug is allocated only once per pharmacy
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyDrugAllocation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "pharmacy_id", nullable = false)
  private Pharmacy pharmacy;

  @ManyToOne(optional = false)
  @JoinColumn(name = "drug_id", nullable = false)
  private Drug drug;

  @Column(name = "allocated", nullable = false)
  private int allocated; // Total amount of this drug allocated to the pharmacy

}