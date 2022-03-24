package com.volunteer.api.service;

import org.apache.commons.lang3.tuple.Pair;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;

public interface VerificationCodeService {

  Pair<Boolean, String> getOrCreate(final VPUser user, VerificationCodeType codeType);

  boolean matches(final VPUser user, final String code, VerificationCodeType codeType);

  void cleanup(VPUser current, VerificationCodeType phone);

}
