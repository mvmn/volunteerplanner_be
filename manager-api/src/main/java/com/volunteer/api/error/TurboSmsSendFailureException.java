package com.volunteer.api.error;

import com.volunteer.api.data.model.turbosms.TurboSmsResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TurboSmsSendFailureException extends RuntimeException {
  private static final long serialVersionUID = -1813798060333120872L;

  @Getter
  private final TurboSmsResponse sendResult;

  @Override
  public String getMessage() {
    return sendResult.toString();
  }
}
