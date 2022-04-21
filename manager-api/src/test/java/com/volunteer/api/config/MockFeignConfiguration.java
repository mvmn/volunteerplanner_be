package com.volunteer.api.config;

import com.volunteer.api.data.model.recaptcha.ReCaptchaVerifyResponse;
import com.volunteer.api.data.model.turbosms.TurboSmsResponse;
import com.volunteer.api.feign.ReCaptchaClient;
import com.volunteer.api.feign.TurboSmsClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockFeignConfiguration {

  @Bean
  public TurboSmsClient turboSmsMock() {
    TurboSmsClient mock = Mockito.mock(TurboSmsClient.class);
    Mockito.when(mock.send(Mockito.any(), Mockito.any()))
        .thenReturn(TurboSmsResponse.builder().responseCode(0).build());
    return mock;
  }

  @Bean
  public ReCaptchaClient reCaptchaClient() {
    ReCaptchaClient mock = Mockito.mock(ReCaptchaClient.class);
    Mockito.when(mock.siteverify(Mockito.any()))
        .thenReturn(ReCaptchaVerifyResponse.builder().success(true).build());
    return mock;
  }
}
