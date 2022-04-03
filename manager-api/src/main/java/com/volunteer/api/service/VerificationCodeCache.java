package com.volunteer.api.service;

import java.util.Optional;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import org.apache.commons.lang3.tuple.Pair;

public interface VerificationCodeCache {

  Optional<String> getCode(VPUser user, VerificationCodeType type);

  Pair<Boolean, String> getOrCreateCode(VPUser user, VerificationCodeType type);

  void delete(VPUser user, VerificationCodeType type);
}
