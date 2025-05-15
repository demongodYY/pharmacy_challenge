package com.pharma.prescription.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="drugs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drug {
  @Id // Marks this field as the primary key
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

  @Column(nullable = false)
  private int stock; // Represents the current available quantity of the drug


  public boolean isExpired() {
    return LocalDate.now().isAfter(this.expiryDate);
  }

}
