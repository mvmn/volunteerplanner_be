package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeDtoV1 {

  @NotBlank
  @JsonProperty("old")
  private String oldPassword;
  @NotBlank
  @JsonProperty("new")
  private String newPassword;

}
