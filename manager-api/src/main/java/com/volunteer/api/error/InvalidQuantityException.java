package com.volunteer.api.error;

public class InvalidQuantityException extends RuntimeException {
  private static final long serialVersionUID = -8245665731322324185L;

    public InvalidQuantityException(String message) {
        super(message);
    }
}
