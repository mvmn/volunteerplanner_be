package com.volunteer.api.data.model.recaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReCaptchaVerifyResponse {
  @JsonProperty("success")
  private Boolean success;
  @JsonProperty("hostname")
  private String hostname;
  @JsonProperty("challenge_ts")
  private String challengeTimestamp;
  @JsonProperty("error-codes")
  private JsonNode errorCodes;
}
