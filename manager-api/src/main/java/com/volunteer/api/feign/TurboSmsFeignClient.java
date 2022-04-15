package com.volunteer.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.volunteer.api.data.model.turbosms.TurboSmsResponse;
import com.volunteer.api.data.model.turbosms.TurboSmsSendRequest;

@FeignClient(name = "turboSms", url = "${turbosms.baseurl:https://api.turbosms.ua}")
public interface TurboSmsFeignClient {

  @PostMapping(value = "/message/send.json", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  TurboSmsResponse send(@RequestBody TurboSmsSendRequest request,
      @RequestHeader("Authorization") String auth);
}
