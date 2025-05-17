package com.pharma.prescription.repository;

import com.pharma.prescription.entity.PharmacyDrugAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PharmacyDrugAllocationRepository extends JpaRepository<PharmacyDrugAllocation, Long> {
  @Query("SELECT pda FROM PharmacyDrugAllocation pda " +
          "WHERE pda.pharmacy.pharmacyId = :pharmacyId " +
          "AND pda.drug.drugId = :drugId")
  Optional<PharmacyDrugAllocation> findByPharmacyPublicIdAndDrugPublicId(
          @Param("pharmacyId") UUID pharmacyId,
          @Param("drugId") UUID drugId
  );

  @Query("SELECT pda FROM PharmacyDrugAllocation pda " +
          "WHERE pda.drug.drugId = :drugId")
  Optional<PharmacyDrugAllocation> findByDrugId(UUID drugId);
}
