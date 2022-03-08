package com.volunteer.api.error;

public class ObjectAlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = 3983637175763918467L;

  public ObjectAlreadyExistsException(final String message) {
    super(message);
  }

}
