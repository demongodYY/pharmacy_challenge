package com.pharma.prescription.exception;

import java.time.LocalDateTime;
import java.util.Map;

public interface IErrorResponse {
  LocalDateTime getTimestamp();

  int getStatus();

  String getError();

  String getMessage();

  Map<String, String> getFieldErrors();
}
