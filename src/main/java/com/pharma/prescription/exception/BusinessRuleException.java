package com.pharma.prescription.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Or Unprocessable Entity (422)
public class BusinessRuleException extends RuntimeException {
  public BusinessRuleException(String message) {
    super(message);
  }

  public BusinessRuleException(String message, Throwable cause) {
    super(message, cause);
  }
}