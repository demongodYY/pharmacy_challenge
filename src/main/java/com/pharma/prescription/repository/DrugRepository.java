package com.pharma.prescription.repository;

import com.pharma.prescription.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Drug entity.
 */
@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {

  /**
   * Finds drugs that are not expired (expiry date is after the given date).
   * @param date The date to compare against the expiry date. Typically, this would be the current date.
   * @return A list of non-expired drugs.
   */
  List<Drug> findByExpiryDateAfter(LocalDate date);

  /**
   * Finds a drug by its name and manufacturer and batch number.
   * This can be useful to uniquely identify a specific batch of a drug.
   * @param name The name of the drug.
   * @param manufacturer The manufacturer of the drug.
   * @param batchNumber The batch number of the drug.
   * @return An Optional containing the drug if found.
   */
  Optional<Drug> findByNameAndManufacturerAndBatchNumber(String name, String manufacturer, String batchNumber);

  /**
   * Finds all drugs with stock greater than zero and not expired.
   * @param currentDate The current date to check against expiry.
   * @return A list of available and non-expired drugs.
   */
  @Query("SELECT d FROM Drug d WHERE d.stock > 0 AND d.expiryDate > :currentDate")
  List<Drug> findAvailableAndNotExpiredDrugs(@Param("currentDate") LocalDate currentDate);
}
