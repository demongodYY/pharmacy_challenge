package com.pharma.prescription.repository;

import com.pharma.prescription.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Drug entity.
 */
@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {
}
