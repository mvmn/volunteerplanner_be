package com.volunteer.api.error;

public class InvalidStatusException extends RuntimeException {
  private static final long serialVersionUID = -1079521018375212289L;

  public InvalidStatusException(String message) {
    super(message);
  }
}
