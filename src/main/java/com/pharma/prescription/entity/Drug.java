package com.pharma.prescription.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @Getter
  @Column(name = "drug_id", updatable = false, nullable = false, unique = true)
  private UUID drugId;

  @Getter
  @Column(nullable = false)
  private String name;

  @Getter
  @Column(nullable = false)
  private String manufacturer;

  @Getter
  @Column(name = "batch_number", nullable = false)
  private String batchNumber;

  @Getter
  @Column(name = "expiry_date", nullable = false)
  private LocalDate expiryDate;

  @Getter
  @OneToMany(mappedBy = "drug", fetch = FetchType.LAZY)
  private Set<PharmacyDrugAllocation> pharmacyAllocations;

  @Getter
  @OneToMany(mappedBy = "drug", fetch = FetchType.LAZY)
  private Set<PrescriptionItem> prescriptionItems;

  @Getter
  @Setter
  @Column(nullable = false)
  private int stock;

  @PrePersist
  protected void onCreate() {
    if (this.drugId == null) {
      this.drugId = UUID.randomUUID();
    }
  }

  public Drug(String name, String manufacturer, String batchNumber, LocalDate expiryDate, int stock) {
    this.name = name;
    this.manufacturer = manufacturer;
    this.batchNumber = batchNumber;
    this.expiryDate = expiryDate;
    this.stock = stock;
  }

  public boolean isExpired() {
    return LocalDate.now().isAfter(this.expiryDate);
  }
}
