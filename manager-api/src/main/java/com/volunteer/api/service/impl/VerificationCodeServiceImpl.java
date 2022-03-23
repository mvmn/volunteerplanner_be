package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.service.VerificationCodeService;
import com.volunteer.api.service.VerificationCodesCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

  private final VerificationCodesCache cache;

  @Override
  public String create(final VPUser user) {
    return cache.getCode(user.getId());
  }

  @Override
  public boolean matches(final VPUser user, final String code) {
    return cache.getCode(user.getId()).equals(code);
  }
}
