package com.volunteer.api.data.user.model;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

public enum Role {

  ADMIN("ROLE_ADMIN"),
  MANAGER("ROLE_MANAGER"),
  VOLUNTEER("ROLE_VOLUNTEER");

  private final String value;

  Role(final String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public Role fromStringValue(final String value) {
    if (StringUtils.isBlank(value)) {
      throw new IllegalArgumentException("Role can't be blank");
    }

    return Arrays.stream(Role.values())
        .filter(role -> role.getValue().equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("Role '%s' does not exist", value)));
  }

}
