package com.volunteer.api.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
@EnableFeignClients(basePackages = "com.volunteer.api.feign")
public class FeignConfiguration {

}
