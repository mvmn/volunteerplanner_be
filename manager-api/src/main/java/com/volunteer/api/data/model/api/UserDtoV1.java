package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.annotation.RegEx;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "phoneNumber", "password", "role", "displayName", "organization",
    "rating", "phoneNumberVerified", "userVerified", "userVerifiedByUserId", "userVerifiedAt",
    "locked", "lockedByUserId", "lockedAt"})
@JsonInclude(Include.NON_NULL)
public class UserDtoV1 {

  @JsonProperty("id")
  private Integer id;

  @NotBlank
  @Pattern(regexp = "^[0-9]{9,15}$")
  @JsonProperty("phoneNumber")
  private String phoneNumber;
  @JsonProperty("password")
  private String password;

  @NotBlank
  @JsonProperty("role")
  private String role;

  @NotBlank
  @JsonProperty("displayName")
  private String displayName;
  @JsonProperty("organization")
  private String organization;

  @JsonProperty("rating")
  private Integer rating;

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
