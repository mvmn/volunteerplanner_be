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

  @Value("${turbosms.enabled:false}")
  private boolean enabled;

  @Override
  public void send(final VPUser user, final String message) {
    if (!enabled) {
      LOG.info("SMS send disabled. SMS to {}: {}", user.getPhoneNumber(), message);
      return;
    }
    TurboSmsResponse result;
    String phoneNum = user.getPhoneNumber();
    if (phoneNum.length() < 11) {
      phoneNum = "38" + phoneNum;
    }
    try {
      LOG.info("Sending SMS message to {}", phoneNum);
      result = turboSms.send(
          TurboSmsSendRequest.builder().recipients(Arrays.asList(phoneNum))
              .sms(TurboSmsMessageData.builder().sender(senderId).text(message).build()).build(),
          auth);
    } catch (Exception e) {
      LOG.error("SMS service communication failure when sending to " + phoneNum, e);
      throw new SmsServiceCommunicationException(e);
    }
    // See https://turbosms.ua/ua/api.html for response codes
    if (result != null && result.getResponseCode() != 0 && result.getResponseCode() != 800
        && result.getResponseCode() != 801) {
      LOG.error("SMS send to {} failure: {}", phoneNum, result);
      throw new TurboSmsSendFailureException(result);
    }
  }
}
