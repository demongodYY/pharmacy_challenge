package com.pharma.prescription.repository;

import com.pharma.prescription.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
  Optional<Pharmacy> findByPharmacyId(UUID pharmacyId);
}
