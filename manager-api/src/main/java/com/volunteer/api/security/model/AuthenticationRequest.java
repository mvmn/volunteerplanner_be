package com.volunteer.api.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthenticationRequest {

  @JsonProperty("username")
  private String userName;
  @JsonProperty("password")
  private String password;

}
