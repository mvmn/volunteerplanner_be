package com.volunteer.api.service;

import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;

public interface VerificationCodesCache {
  Optional<String> getCode(VPUser user, VerificationCodeType type);

  Pair<Boolean, String> getOrCreateCode(VPUser user, VerificationCodeType type);

  void delete(VPUser user, VerificationCodeType type);
}
