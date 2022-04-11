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
public class TurboSmsMessageData {

  @JsonProperty("sender")
  private String sender;
  @JsonProperty("text")
  private String text;
}
