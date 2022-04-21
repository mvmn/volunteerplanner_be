package com.volunteer.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import com.volunteer.api.data.model.recaptcha.ReCaptchaVerifyRequest;
import com.volunteer.api.data.model.recaptcha.ReCaptchaVerifyResponse;

@FeignClient(name = "reCaptcha", url = "${recaptcha.baseurl:https://www.google.com/recaptcha/api}",
    configuration = ReCaptchaClientConfig.class)
public interface ReCaptchaClient {

  @PostMapping(path = "/siteverify", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  ReCaptchaVerifyResponse siteverify(ReCaptchaVerifyRequest request);
}
