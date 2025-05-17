package com.pharma.prescription.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "pharmacies")
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacy {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "pharmacy_id", updatable = false, nullable = false, unique = true)
  private UUID pharmacyId;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String address;

  @OneToMany(
          mappedBy = "pharmacy",
          cascade = CascadeType.ALL,
          fetch = FetchType.LAZY,
          orphanRemoval = true)
  private Set<PharmacyDrugAllocation> drugAllocations = new HashSet<>();

  @OneToMany(mappedBy = "pharmacy", fetch = FetchType.LAZY)
  private Set<Prescription> prescriptions;

  public Pharmacy(String name, String address) {
    this.name = name;
    this.address = address;
  }

  @PrePersist
  protected void onCreate() {
    if (this.pharmacyId == null) {
      this.pharmacyId = UUID.randomUUID();
    }
  }
}
