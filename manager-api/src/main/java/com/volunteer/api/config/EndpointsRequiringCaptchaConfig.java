package com.volunteer.api.config;

import java.util.Collections;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "captcha")
@Data
public class EndpointsRequiringCaptchaConfig {

  private Set<String> endpoints = Collections.emptySet();
}
