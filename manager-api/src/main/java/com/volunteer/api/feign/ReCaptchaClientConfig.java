package com.volunteer.api.feign;

import org.springframework.context.annotation.Bean;
import feign.codec.Encoder;
import feign.form.FormEncoder;

public class ReCaptchaClientConfig {

  @Bean
  Encoder formEncoder() {
    return new FormEncoder();
  }
}
