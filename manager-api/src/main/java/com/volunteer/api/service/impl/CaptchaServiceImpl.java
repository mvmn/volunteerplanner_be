package com.volunteer.api.service.impl;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.volunteer.api.data.model.recaptcha.ReCaptchaVerifyRequest;
import com.volunteer.api.data.model.recaptcha.ReCaptchaVerifyResponse;
import com.volunteer.api.error.InvalidCaptchaException;
import com.volunteer.api.feign.ReCaptchaClient;
import com.volunteer.api.service.CaptchaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

  private final ReCaptchaClient reCaptchaClient;

  @Value("${recaptcha.secret:}")
  private String reCaptchaSecretKey;

  @Override
  public void validateCaptchaResponse(HttpServletRequest request) {
    boolean result = false;

    String captchaResponse = request.getHeader(CaptchaService.CAPTCHA_HEADER_NAME);
    if (StringUtils.isNotBlank(captchaResponse)) {
      ReCaptchaVerifyResponse response = reCaptchaClient.siteverify(ReCaptchaVerifyRequest.builder()
          .secret(reCaptchaSecretKey).response(captchaResponse).build());
      result = response.getSuccess() != null && response.getSuccess().booleanValue();
    }
    if (!result) {
      throw new InvalidCaptchaException("Bad captcha");
    }
  }
}
