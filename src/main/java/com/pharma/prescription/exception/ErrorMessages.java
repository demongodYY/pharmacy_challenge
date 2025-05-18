package com.pharma.prescription.exception;

import com.pharma.prescription.entity.Drug;
import com.pharma.prescription.entity.Prescription;
import com.pharma.prescription.entity.PrescriptionItem;

import java.util.UUID;

public class ErrorMessages {
  public static String getFulfilmentStockOverflowFailureMessage(PrescriptionItem item, Drug drug) {
    return "Insufficient stock for drug " +
            drug.getName() + " during fulfillment. Requested: " +
            item.getDosage() + ", Available: " +
            drug.getStock() + ". ";
  }

  public static String getFulfilmentConcurrentFailedMessage() {
    return "Fulfillment failed due to concurrent drug stock update.";
  }

  public static String getPrescriptionItemsCountMessage() {
    return "Prescription must contain at least one item";
  }

  public static String getPrescriptionStatusErrorMessage(Prescription prescription) {
    return "Prescription is not in a state that can be fulfilled: " + prescription.getStatus();
  }

  public static String getDrugExpiredMessage(Drug drug) {
    return "Drug is expired: " + drug.getDrugId();
  }

  public static String getNotAllocationMessage(UUID dragId, UUID pharmacyId) {
    return "Drug: " + dragId + "is not allocated for this pharmacy: " + pharmacyId;
  }

  public static String getDrugNotContractedMessage(Drug drug) {
    return "Pharmacy is not contracted for this drug: " + drug.getDrugId();
  }

  public static String getDosageExceedsAllocatedMessage(Integer dosage) {
    return "Dosage exceeds allocated amount: " + dosage;
  }

  public static String getAllocatedAmountExceedsStockMessage(Integer totalAllocated, Integer stock) {
    return "Total allocated amount: " + totalAllocated + " exceeds total stock: " + stock;
  }

  public static String getNotFoundMessage(String entityName, UUID id) {
    return entityName + " not found: " + id;
  }
}
