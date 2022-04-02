package com.volunteer.api.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {

  @NotBlank
  @NotNull
  @JsonProperty("principal")
  private String principal;

  @NotBlank
  @NotNull
  @JsonProperty("password")
  private String password;

}
