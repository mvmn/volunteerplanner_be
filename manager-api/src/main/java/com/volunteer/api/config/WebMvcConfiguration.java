package com.volunteer.api.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.volunteer.api.interceptor.AuditInterceptor;
import com.volunteer.api.interceptor.CaptchaInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

  @Autowired
  private ObjectProvider<AuditInterceptor> auditInterceptor;

  @Autowired
  private ObjectProvider<CaptchaInterceptor> captchaInterceptor;

  @Value("${cors.enable:false}")
  private boolean enableCors;

  @Value("${cors.credentials:true}")
  private boolean corsAllowCredentials = true;

  @Value("${cors.origins:}")
  private String corsAllowedOrigins = "";

  @Value("${cors.headers:*}")
  private String corsAllowedHeaders = "*";

  @Value("${cors.methods:*}")
  private String corsAllowedMethods = "*";

  @Value("${cors.exposeheaders:*}")
  private String corsExposedHeaders = "*";

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(auditInterceptor.getIfUnique());
    registry.addInterceptor(captchaInterceptor.getIfUnique());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    if (enableCors) {
      registry.addMapping("/**").allowedOriginPatterns(corsAllowedOrigins)
          .allowCredentials(corsAllowCredentials).allowedHeaders(corsAllowedHeaders)
          .exposedHeaders(corsExposedHeaders).allowedMethods(corsAllowedMethods);
    }
  }
}
