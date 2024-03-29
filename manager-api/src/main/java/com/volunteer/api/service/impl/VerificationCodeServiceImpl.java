package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import com.volunteer.api.service.VerificationCodeCache;
import com.volunteer.api.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

  private final VerificationCodeCache cache;

  @Override
  public Pair<Boolean, String> getOrCreate(final VPUser user, final VerificationCodeType codeType) {
    return cache.getOrCreateCode(user, codeType);
  }

  @Override
  public boolean matches(final VPUser user, final VerificationCodeType codeType,
      final String code) {
    return cache.getCode(user, codeType).filter(existingCode -> existingCode.equals(code))
        .isPresent();
  }

  @Override
  public void cleanup(VPUser user, VerificationCodeType type) {
    cache.delete(user, type);
  }

}
