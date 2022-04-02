package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import com.volunteer.api.service.VerificationCodeCache;
import com.volunteer.api.service.VerificationCodeGenerator;
import com.volunteer.api.service.VerificationCodeService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

  private final VerificationCodeCache cache;
  private final VerificationCodeGenerator generator;

  @Override
  public Pair<Boolean, String> getOrCreate(final VPUser user, final VerificationCodeType codeType) {
    final Optional<String> current = cache.get(user, codeType);
    if (current.isPresent()) {
      return Pair.of(false, current.get());
    }

    final String code = generator.generate();
    cache.put(user, codeType, code);

    return Pair.of(true, code);
  }

  @Override
  public boolean matches(final VPUser user, final VerificationCodeType codeType,
      final String code) {
    final Optional<String> current = cache.get(user, codeType);
    if (current.isEmpty()) {
      return false;
    }

    return current.get().equals(code);
  }

  @Override
  public void cleanup(final VPUser user, final VerificationCodeType type) {
    cache.delete(user, type);
  }

}
