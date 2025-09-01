package com.telecom.exceptions;

public class PlanNotFoundException extends RuntimeException {
  public PlanNotFoundException(String message) {
    super(message);
  }
}
