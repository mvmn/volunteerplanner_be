package com.volunteer.api.data.model.recaptcha;

import feign.form.FormProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReCaptchaVerifyRequest {

  @FormProperty("secret")
  private String secret;

  @FormProperty("response")
  private String response;
}
