package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import com.volunteer.api.data.repository.VerificationCodeRepository;
import com.volunteer.api.service.VerificationCodeCache;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class VerificationCodesDbCacheImpl implements VerificationCodeCache {

  private final Duration valueTtl;
  private final VerificationCodeRepository repository;

  @Override
  @Transactional
  public Optional<String> get(final VPUser user, final VerificationCodeType type) {
    final Optional<VerificationCode> result = repository.findByUserAndType(user, type);
    if (result.isEmpty()) {
      return Optional.empty();
    }

    if (isExpired(result.get())) {
      delete(user, type);
      return Optional.empty();
    }

    return Optional.of(result.get().getCode());
  }

  @Override
  @Transactional
  public void put(final VPUser user, final VerificationCodeType type, final String code) {
    final VerificationCode entity = new VerificationCode();
    entity.setType(type);
    entity.setUser(user);
    entity.setCode(code);
    entity.setCreatedAt(Instant.now().getEpochSecond());

    repository.save(entity);
  }

  @Override
  @Transactional
  public void delete(final VPUser user, final VerificationCodeType type) {
    repository.deleteByUserAndType(user, type);
  }

  private boolean isExpired(final VerificationCode code) {
    return Instant.now().minus(valueTtl).toEpochMilli() <= code.getCreatedAt();
  }

}
