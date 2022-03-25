package com.volunteer.api.service.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import com.volunteer.api.data.repository.VerificationCodeRepository;
import com.volunteer.api.service.VerificationCodeGenerator;
import com.volunteer.api.service.VerificationCodesCache;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationCodesCacheImpl implements VerificationCodesCache {

  protected final VerificationCodeGenerator generator;
  protected final VerificationCodeRepository repo;

  @Value("${vp.verificationcodes.ttlmin:10}")
  private Integer verificationCodesCacheTtlMin = 10;

  @Override
  public Optional<String> getCode(VPUser user, VerificationCodeType type) {
    return repo
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
      VerificationCode code = new VerificationCode();
      code.setType(type);
      code.setUser(user);
      code.setCode(generator.generateRandomCode());
      code.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond());
      repo.deleteByTypeAndUserAndCreatedAtLessThan(type, user, getTimestampValidityThreshold());
      code = repo.save(code);
      return Pair.of(true, code.getCode());
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
    return ZonedDateTime.now(ZoneOffset.UTC).minus(verificationCodesCacheTtlMin, ChronoUnit.MINUTES)
        .toEpochSecond();
  }

  @Override
  public void delete(VPUser user, VerificationCodeType type) {
    repo.deleteByTypeAndUser(type, user);
  }
}
