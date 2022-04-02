package com.volunteer.api.service;

import org.apache.commons.lang3.tuple.Pair;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;

public interface VerificationCodeService {

  Pair<Boolean, String> getOrCreate(final VPUser user, final VerificationCodeType codeType);

  boolean matches(final VPUser user, final VerificationCodeType codeType, final String code);

  void cleanup(final VPUser current, final VerificationCodeType phone);

}
