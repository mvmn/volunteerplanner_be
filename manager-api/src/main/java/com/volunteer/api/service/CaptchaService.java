package com.volunteer.api.service;

import javax.servlet.http.HttpServletRequest;

public interface CaptchaService {

  public static final String CAPTCHA_HEADER_NAME = "X-Captcha";

  public void validateCaptchaResponse(HttpServletRequest request);
}
