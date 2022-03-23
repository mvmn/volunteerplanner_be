package com.volunteer.api.service;

import com.volunteer.api.data.model.persistence.VPUser;

public interface VerificationCodeService {

  String create(final VPUser user);

  boolean matches(final VPUser user, final String code);

}
