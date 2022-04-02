package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

  private static final Logger LOG = LoggerFactory.getLogger(SmsServiceImpl.class);

  @Override
  public void send(final VPUser user, final String message) {
    LOG.info("SMS to " + user.getDisplayName() + ": "
        + message); // FIXME: Remove and     // implement functionality
  }

}
