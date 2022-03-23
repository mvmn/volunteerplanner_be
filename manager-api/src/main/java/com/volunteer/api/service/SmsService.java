package com.volunteer.api.service;

import com.volunteer.api.data.model.persistence.VPUser;

public interface SmsService {

  void send(final VPUser user, final String message);

}
