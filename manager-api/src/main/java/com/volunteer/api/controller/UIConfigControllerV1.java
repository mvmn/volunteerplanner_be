package com.volunteer.api.controller;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.volunteer.api.data.model.api.UIConfigDtoV1;
import com.volunteer.api.data.model.api.UIConfigDtoV1.UICaptchaConfig;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UIConfigControllerV1 {

  @Value("${captcha.enable:false}")
  private boolean enableCaptcha;

  @Value("${recaptcha.sitekey:}")
  private String reCaptchaSiteKey;

  private UIConfigDtoV1 config;

  @PostConstruct
  public void init() {
    config = UIConfigDtoV1.builder()
        .captcha(UICaptchaConfig.builder().enabled(enableCaptcha).sitekey(reCaptchaSiteKey).build())
        .build();
  }


  @GetMapping("config")
  public UIConfigDtoV1 config() {
    return config;
  }
}
