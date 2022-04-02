package com.volunteer.api.service;

import java.util.Optional;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;

public interface VerificationCodeCache {

  Optional<String> get(final VPUser user, final VerificationCodeType type);

  void put(final VPUser user, final VerificationCodeType type, final String value);

  void delete(VPUser user, VerificationCodeType type);

}
