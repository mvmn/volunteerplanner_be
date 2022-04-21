package com.volunteer.api.error;

public class InvalidCaptchaException extends RuntimeException {
  private static final long serialVersionUID = -6202993774771691418L;

  public InvalidCaptchaException() {
    super();
  }

  public InvalidCaptchaException(String message) {
    super(message);
  }
}
