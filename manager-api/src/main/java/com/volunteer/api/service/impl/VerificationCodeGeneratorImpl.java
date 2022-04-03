package com.volunteer.api.service.impl;

import com.volunteer.api.service.VerificationCodeGenerator;
import java.security.SecureRandom;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeGeneratorImpl implements VerificationCodeGenerator {

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  @Override
  public String generateRandomCode() {
    return String.format("%06d", SECURE_RANDOM.nextInt(1000000));
  }

}
