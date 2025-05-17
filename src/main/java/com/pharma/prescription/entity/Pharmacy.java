package com.pharma.prescription.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "pharmacies")
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacy {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Column(name = "pharmacy_id", updatable = false, nullable = false, unique = true)
  private UUID pharmacyId;

  @Getter
  @Setter
  @Column(nullable = false, unique = true)
  private String name;

  @Getter
  @Setter
  @Column(nullable = false)
  private String address;

  @Getter
  @OneToMany(
          mappedBy = "pharmacy",
          cascade = CascadeType.ALL,
          fetch = FetchType.LAZY,
          orphanRemoval = true)
  private Set<PharmacyDrugAllocation> drugAllocations = new HashSet<>();

  @Getter
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
