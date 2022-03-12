package com.volunteer.api.data.user.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "userName", "phoneNumber", "password", "role", "fullName", "email"})
@JsonInclude(Include.NON_NULL)
public class UserDtoV1 {

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("userName")
  private String userName;
  @JsonProperty("phoneNumber")
  private String phoneNumber;
  @JsonProperty("password")
  private String password;

  @JsonProperty("role")
  private String role;

  @JsonProperty("fullName")
  private String fullName;
  @JsonProperty("email")
  private String email;

}
