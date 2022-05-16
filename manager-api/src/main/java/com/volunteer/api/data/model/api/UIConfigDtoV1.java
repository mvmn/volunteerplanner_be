package com.volunteer.api.data.model.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UIConfigDtoV1 {

  @Data
  @Builder
  public static class UICaptchaConfig {
    private final boolean enabled;
    private final String sitekey;
  }

  private final UICaptchaConfig captcha;
}
