package com.pharma.prescription.repository;

import com.pharma.prescription.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the Drug entity.
 */
@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {
  Optional<Drug> findByDrugId(UUID drugId);
}
