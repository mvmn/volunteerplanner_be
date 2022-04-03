package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import com.volunteer.api.data.repository.VerificationCodeRepository;
import com.volunteer.api.service.VerificationCodeCache;
import com.volunteer.api.service.VerificationCodeGenerator;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
public class VerificationCodeDbCacheImpl implements VerificationCodeCache {

  private final Duration valueTtl;
  private final VerificationCodeRepository repository;

  private final VerificationCodeGenerator generator;

  @Override
  public Optional<String> getCode(VPUser user, VerificationCodeType type) {
    return repository
        .findByTypeAndUserAndCreatedAtGreaterThan(type, user, getTimestampValidityThreshold())
        .map(VerificationCode::getCode);
  }

  @Override
  public Pair<Boolean, String> getOrCreateCode(VPUser user, VerificationCodeType type) {
    Optional<String> existingCode = getCode(user, type);
    if (existingCode.isPresent()) {
      return Pair.of(false, existingCode.get());
    }
    try {
      VerificationCode entity = new VerificationCode();
      entity.setType(type);
      entity.setUser(user);
      entity.setCode(generator.generateRandomCode());
      entity.setCreatedAt(Instant.now().getEpochSecond());

      repository.deleteByTypeAndUserAndCreatedAtLessThan(type, user,
          getTimestampValidityThreshold());
      entity = repository.save(entity);
      return Pair.of(true, entity.getCode());
    } catch (DataIntegrityViolationException exception) {
      if (exception.getCause() != null
          && exception.getCause() instanceof ConstraintViolationException) {
        ConstraintViolationException constraintViolation =
            (ConstraintViolationException) exception.getCause();
        if (constraintViolation.getConstraintName().equals("uq_verification_code_user_type")) {
          return Pair.of(false, getCode(user, type).get());
        }
      }
      throw exception;
    }
  }

  protected long getTimestampValidityThreshold() {
    return Instant.now().minus(valueTtl).getEpochSecond();
  }

  @Override
  public void delete(VPUser user, VerificationCodeType type) {
    repository.deleteByTypeAndUser(type, user);
  }
}
