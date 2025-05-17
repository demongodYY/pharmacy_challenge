package com.pharma.prescription.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "drugs")
@NoArgsConstructor
@AllArgsConstructor
public class Drug {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures auto-generation of the ID
  private Long id;

  @Column(name = "drug_id", updatable = false, nullable = false, unique = true)
  private UUID drugId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String manufacturer;

  @Column(name = "batch_number", nullable = false)
  private String batchNumber;

  @Column(name = "expiry_date", nullable = false)
  private LocalDate expiryDate;

  @OneToMany(mappedBy = "drug", fetch = FetchType.LAZY)
  private Set<PharmacyDrugAllocation> pharmacyAllocations;

  @OneToMany(mappedBy = "drug")
  private Set<PrescriptionItem> prescriptionItems;

  @Column(nullable = false)
  private int stock;

  @PrePersist
  protected void onCreate() {
    if (this.drugId == null) {
      this.drugId = UUID.randomUUID();
    }
  }

  public boolean isExpired() {
    return LocalDate.now().isAfter(this.expiryDate);
  }
}
