package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.service.SmsService;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

  @Override
  public void send(final VPUser user, final String message) {
    System.out.println("SMS to " + user.getUserName() + ": " + message); // FIXME: Remove and
                                                                         // implement functionality
  }

}
