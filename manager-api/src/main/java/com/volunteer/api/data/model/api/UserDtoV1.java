package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "userName", "phoneNumber", "password", "role", "fullName", "email",
    "city", "phoneNumberVerified", "userVerified", "userVerifiedByUserId", "userVerifiedAt",
    "locked", "lockedByUserId", "lockedAt"})
@JsonInclude(Include.NON_NULL)
public class UserDtoV1 {

  @JsonProperty("id")
  private Integer id;

  @NotBlank
  @JsonProperty("userName")
  private String userName;
  @NotBlank
  @JsonProperty("phoneNumber")
  private String phoneNumber;
  @JsonProperty("password")
  private String password;

  @NotBlank
  @JsonProperty("role")
  private String role;

  @JsonProperty("fullName")
  private String fullName;
  @JsonProperty("email")
  private String email;

  @JsonProperty("phoneNumberVerified")
  private Boolean phoneNumberVerified;

  @JsonProperty("userVerified")
  private Boolean userVerified;
  @JsonProperty("userVerifiedByUserId")
  private Integer userVerifiedByUserId;
  @Column(name = "userVerifiedAt")
  private Long userVerifiedAt;

  @JsonProperty("locked")
  private Boolean locked;
  @JsonProperty("lockedByUserId")
  private Integer lockedByUserId;
  @Column(name = "lockedAt")
  private Long lockedAt;

}
