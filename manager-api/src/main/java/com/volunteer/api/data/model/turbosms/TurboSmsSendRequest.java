package com.volunteer.api.data.model.turbosms;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurboSmsSendRequest {

  @JsonProperty("recipients")
  private List<String> recipients;
  @JsonProperty("sms")
  private TurboSmsMessageData sms;
}
