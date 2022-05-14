package com.volunteer.api.data.model.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "phoneNumber", "password", "role", "displayName", "organization",
    "rating", "phoneNumberVerified", "userVerified", "userVerifiedBy", "userVerifiedByUserId",
    "userVerifiedAt", "locked", "lockedByUserId", "lockedAt"})
@JsonInclude(Include.NON_NULL)
public class UpdateCurrentUserDtoV1 {

  @Pattern(regexp = "^[0-9]{12}|$")
  @JsonProperty("phoneNumber")
  private String phoneNumber;

  @NotBlank
  @JsonProperty("displayName")
  private String displayName;
  @JsonProperty("organization")
  private String organization;
}
