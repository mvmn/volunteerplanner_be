package com.volunteer.api.error;

public class SmsServiceCommunicationException extends RuntimeException {
  private static final long serialVersionUID = -7890261210633147223L;

  public SmsServiceCommunicationException() {
    super();
  }

  public SmsServiceCommunicationException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public SmsServiceCommunicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public SmsServiceCommunicationException(String message) {
    super(message);
  }

  public SmsServiceCommunicationException(Throwable cause) {
    super(cause);
  }
}
