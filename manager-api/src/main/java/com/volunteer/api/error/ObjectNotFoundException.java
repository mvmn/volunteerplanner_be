package com.volunteer.api.error;

public class ObjectNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -7299803603977055965L;

  public ObjectNotFoundException(final String message) {
    super(message);
  }

}
