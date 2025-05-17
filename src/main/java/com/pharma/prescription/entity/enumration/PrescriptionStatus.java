package com.pharma.prescription.entity.enumration;

public enum PrescriptionStatus {
  PENDING,
  FULFILLED,
  FAILED_VALIDATION, // Initial check failed (e.g., drug not contracted, allocation exceeded)
  FAILED_FULFILLMENT // Fulfillment attempt failed (e.g., stock insufficient during fulfillment)
}