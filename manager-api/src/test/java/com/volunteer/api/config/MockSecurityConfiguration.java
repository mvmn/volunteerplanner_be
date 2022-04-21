package com.volunteer.api.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerExceptionResolver;

public class MockSecurityConfiguration {

  @Bean
  public HandlerExceptionResolver handlerExceptionResolver() {
    return Mockito.mock(HandlerExceptionResolver.class);
  }

}
