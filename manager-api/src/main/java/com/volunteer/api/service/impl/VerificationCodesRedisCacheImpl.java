package com.volunteer.api.service.impl;

import java.util.Optional;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import com.volunteer.api.config.CachingConfig;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import com.volunteer.api.service.VerificationCodeGenerator;
import com.volunteer.api.service.VerificationCodesCache;
import lombok.RequiredArgsConstructor;

@ConditionalOnProperty(name = "vp.cachetype", havingValue = "redis", matchIfMissing = false)
@Service
@RequiredArgsConstructor
public class VerificationCodesRedisCacheImpl implements VerificationCodesCache {
  protected final VerificationCodeGenerator generator;
  protected final CacheManager cacheManager;
  protected Cache cache;

  @PostConstruct
  public void init() {
    cache = cacheManager.getCache(CachingConfig.VERIFICATION_CODES_CACHE_NAME);
  }

  @Override
  public Optional<String> getCode(VPUser user, VerificationCodeType type) {
    return Optional.ofNullable(cache.get(toCacheKey(user, type))).map(ValueWrapper::get)
        .filter(v -> v != null).map(Object::toString);
  }

  @Override
  public Pair<Boolean, String> getOrCreateCode(VPUser user, VerificationCodeType type) {
    String newCode = generator.generateRandomCode();
    Optional<String> existingCode =
        Optional.ofNullable(cache.putIfAbsent(toCacheKey(user, type), newCode))
            .map(ValueWrapper::get).filter(v -> v != null).map(Object::toString);
    return Pair.of(existingCode.isPresent(), existingCode.orElse(newCode));
  }

  @Override
  public void delete(VPUser user, VerificationCodeType type) {
    cache.evictIfPresent(toCacheKey(user, type));
  }

  private String toCacheKey(VPUser user, VerificationCodeType type) {
    return type.name() + "::" + user.getId();
  }
}
