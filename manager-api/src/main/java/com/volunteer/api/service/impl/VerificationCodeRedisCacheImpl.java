package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import com.volunteer.api.service.VerificationCodeCache;
import com.volunteer.api.service.VerificationCodeGenerator;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

@RequiredArgsConstructor
public class VerificationCodeRedisCacheImpl implements VerificationCodeCache {

  private final Cache cache;
  protected final VerificationCodeGenerator generator;

  @Override
  public Optional<String> getCode(VPUser user, VerificationCodeType type) {
    return Optional.ofNullable(cache.get(toCacheKey(user, type), String.class));
  }

  @Override
  public Pair<Boolean, String> getOrCreateCode(VPUser user, VerificationCodeType type) {
    final String newCode = generator.generateRandomCode();

    final ValueWrapper existingCode = cache.putIfAbsent(toCacheKey(user, type), newCode);
    if (Objects.isNull(existingCode)) {
      return Pair.of(true, newCode);
    }

    return Pair.of(false, (String) existingCode.get());
  }

  @Override
  public void delete(VPUser user, VerificationCodeType type) {
    cache.evictIfPresent(toCacheKey(user, type));
  }

  private String toCacheKey(VPUser user, VerificationCodeType type) {
    return type.name() + "::" + user.getId();
  }

}
