package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.service.VerificationCodeService;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

  // inject cache here

  @Override
  public String create(final VPUser user) {
    // generate random string, 4 digits length
    // put to the cache, userid - random string mapping. live time up to 10 mins
    // return random string

    return null;
  }

  @Override
  public boolean matches(final VPUser user, final String code) {
    // gets random string entered by user
    // compare existing and passed or false if no code exists

    return false;
  }
}
