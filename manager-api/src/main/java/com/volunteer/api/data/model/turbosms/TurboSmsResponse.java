package com.volunteer.api.data.model.turbosms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurboSmsResponse {

  @JsonProperty("response_code")
  private Integer responseCode;

  @JsonProperty("response_status")
  private String responseStatus;

  @JsonProperty("response_result")
  private String responseResult;
}
