package com.pharma.prescription.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a pharmacy in the system.
 */
@Entity
@Table(name = "pharmacies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacy {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String address;

  // Manages the specific drugs this pharmacy is contracted for and their allocations
  @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<PharmacyDrugAllocation> drugAllocations = new HashSet<>();

  // Helper method to add a drug allocation
  public void addDrugAllocation(PharmacyDrugAllocation allocation) {
    drugAllocations.add(allocation);
    allocation.setPharmacy(this);
  }

  // Helper method to remove a drug allocation
  public void removeDrugAllocation(PharmacyDrugAllocation allocation) {
    drugAllocations.remove(allocation);
    allocation.setPharmacy(null);
  }
}
