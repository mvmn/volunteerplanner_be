package com.volunteer.api.config;

import com.volunteer.api.data.repository.VerificationCodeRepository;
import com.volunteer.api.service.VerificationCodeCache;
import com.volunteer.api.service.VerificationCodeGenerator;
import com.volunteer.api.service.impl.VerificationCodeDbCacheImpl;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "cache.type", havingValue = "db", matchIfMissing = true)
@Configuration
public class CacheConfigurationDb {

  @Value("${cache.verification-code-ttl:10m}")
  private Duration verificationCodeTtl;

  @Bean
  public VerificationCodeCache verificationCodeCache(final VerificationCodeRepository repository,
      final VerificationCodeGenerator generator) {
    return new VerificationCodeDbCacheImpl(verificationCodeTtl, repository, generator);
  }

}
