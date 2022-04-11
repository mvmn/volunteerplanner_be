package com.volunteer.api.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.volunteer.api.data.model.turbosms.TurboSmsResponse;
import com.volunteer.api.feign.TurboSmsFeignClient;

@Configuration
public class MockFeignConfig {

  @Bean
  public TurboSmsFeignClient turboSmsMock() {
    TurboSmsFeignClient mock = Mockito.mock(TurboSmsFeignClient.class);
    Mockito.when(mock.send(Mockito.any(), Mockito.any()))
        .thenReturn(TurboSmsResponse.builder().responseCode(0).build());
    return mock;
  }
}
