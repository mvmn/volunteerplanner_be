package com.volunteer.api.config;

import com.volunteer.api.data.model.turbosms.TurboSmsResponse;
import com.volunteer.api.feign.TurboSmsFeignClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockFeignConfiguration {

  @Bean
  public TurboSmsFeignClient turboSmsMock() {
    TurboSmsFeignClient mock = Mockito.mock(TurboSmsFeignClient.class);
    Mockito.when(mock.send(Mockito.any(), Mockito.any()))
        .thenReturn(TurboSmsResponse.builder().responseCode(0).build());
    return mock;
  }

}
