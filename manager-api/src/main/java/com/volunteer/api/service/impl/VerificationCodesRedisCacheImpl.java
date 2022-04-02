package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import com.volunteer.api.service.VerificationCodeCache;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;

@RequiredArgsConstructor
public class VerificationCodesRedisCacheImpl implements VerificationCodeCache {

  private final Cache cache;

  @Override
  public Optional<String> get(final VPUser user, final VerificationCodeType type) {
    return Optional.ofNullable(cache.get(toCacheKey(user, type), String.class));
  }

  @Override
  public void put(final VPUser user, final VerificationCodeType type, final String code) {
    cache.putIfAbsent(toCacheKey(user, type), code);
  }

  @Override
  public void delete(final VPUser user, final VerificationCodeType type) {
    cache.evictIfPresent(toCacheKey(user, type));
  }

  private String toCacheKey(final VPUser user, final VerificationCodeType type) {
    return type.name() + "::" + user.getId();
  }

}
