package com.volunteer.api.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.volunteer.api.feign.TurboSmsFeignClient;

@Configuration
public class MockFeignConfig {

  @Bean
  public TurboSmsFeignClient turboSmsMock() {
    return Mockito.mock(TurboSmsFeignClient.class);
  }
}
