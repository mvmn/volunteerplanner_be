package com.volunteer.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.volunteer.api.audit.AuditInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

  @Autowired
  private AuditInterceptor auditInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(auditInterceptor);
  }
}
