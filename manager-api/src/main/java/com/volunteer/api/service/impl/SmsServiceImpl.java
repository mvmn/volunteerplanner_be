package com.volunteer.api.service.impl;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.turbosms.TurboSmsMessageData;
import com.volunteer.api.data.model.turbosms.TurboSmsResponse;
import com.volunteer.api.data.model.turbosms.TurboSmsSendRequest;
import com.volunteer.api.error.TurboSmsSendFailureException;
import com.volunteer.api.feign.TurboSmsFeignClient;
import com.volunteer.api.service.SmsService;

@Service
public class SmsServiceImpl implements SmsService {

  private static final Logger LOG = LoggerFactory.getLogger(SmsServiceImpl.class);

  @Autowired
  private TurboSmsFeignClient turboSms;

  @Value("${turbosms.sender:}")
  private String senderId;

  @Value("Bearer ${turbosms.authtoken:}")
  private String auth;

  @Override
  public void send(final VPUser user, final String message) {
    LOG.info("Sending verification code SMS to {}", user.getDisplayName());
    // TODO: Error handling
    TurboSmsResponse result = turboSms.send(
        TurboSmsSendRequest.builder().recipients(Arrays.asList(user.getPhoneNumber()))
            .sms(TurboSmsMessageData.builder().sender(senderId).text(message).build()).build(),
        auth);
    if (result != null && result.getResponseCode() != 0) {
      throw new TurboSmsSendFailureException(result);
    }
  }
}
