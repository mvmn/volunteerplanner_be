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
import com.volunteer.api.error.SmsServiceCommunicationException;
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
    TurboSmsResponse result;
    try {
      result = turboSms.send(
          TurboSmsSendRequest.builder().recipients(Arrays.asList(user.getPhoneNumber()))
              .sms(TurboSmsMessageData.builder().sender(senderId).text(message).build()).build(),
          auth);
    } catch (Exception e) {
      LOG.error("SMS service communication failure", e);
      throw new SmsServiceCommunicationException(e);
    }
    // See https://turbosms.ua/ua/api.html for response codes
    if (result != null && result.getResponseCode() != 0 && result.getResponseCode() != 800
        && result.getResponseCode() != 801) {
      LOG.error("SMS send failure: {}", result);
      throw new TurboSmsSendFailureException(result);
    }
  }
}
