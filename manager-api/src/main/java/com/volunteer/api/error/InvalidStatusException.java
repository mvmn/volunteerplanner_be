package com.volunteer.api.error;

public class InvalidStatusException extends RuntimeException {
  public InvalidStatusException(String message) {
    super(message);
  }
}
