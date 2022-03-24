package com.volunteer.api.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.volunteer.api.service.VerificationCodeGenerator;
import com.volunteer.api.service.VerificationCodesCache;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationCodesCacheImpl implements VerificationCodesCache {

  protected final VerificationCodeGenerator verificationCodeGenerator;

  @Override
  @Cacheable(cacheNames = "verificationCodesCache")
  public String getCode(Integer userId) {
    return verificationCodeGenerator.generateRandomCode();
  }
}
