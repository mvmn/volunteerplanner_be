package com.volunteer.api.data.model;

import java.util.Arrays;
import java.util.Set;

public enum UserRole {

  ROOT("root", Set.of(
      UserAuthority.USERS_VIEW,
      UserAuthority.USERS_VERIFY,
      UserAuthority.USERS_LOCK
  )),
  OPERATOR("operator", Set.of(
      UserAuthority.USERS_VIEW,
      UserAuthority.USERS_VERIFY,
      UserAuthority.USERS_LOCK,

      UserAuthority.CATEGORIES_VIEW,
      UserAuthority.CATEGORIES_MODIFY,

      UserAuthority.STORES_VIEW_PUBLIC,
      UserAuthority.STORES_VIEW_CONFIDENTIAL,
      UserAuthority.STORES_MODIFY,

      UserAuthority.PRODUCTS_VIEW,
      UserAuthority.PRODUCTS_MODIFY,

      UserAuthority.TASKS_VIEW,
      UserAuthority.TASKS_MODIFY,
      UserAuthority.TASKS_VERIFY,
      UserAuthority.TASKS_COMPLETE,
      UserAuthority.TASKS_REJECT,

      UserAuthority.SUBTASKS_VIEW,
      UserAuthority.SUBTASKS_MODIFY,
      UserAuthority.SUBTASKS_COMPLETE,
      UserAuthority.SUBTASKS_REJECT
  )),
  REQUESTOR("requestor", Set.of(
      UserAuthority.CATEGORIES_VIEW,
      UserAuthority.CATEGORIES_MODIFY,

      UserAuthority.STORES_VIEW_PUBLIC,
      UserAuthority.STORES_VIEW_CONFIDENTIAL,
      UserAuthority.STORES_MODIFY,

      UserAuthority.PRODUCTS_VIEW,
      UserAuthority.PRODUCTS_MODIFY,

      UserAuthority.TASKS_VIEW,
      UserAuthority.TASKS_MODIFY_MINE,
      UserAuthority.TASKS_REJECT_MINE,

      UserAuthority.SUBTASKS_VIEW,
      UserAuthority.SUBTASKS_MODIFY,
      UserAuthority.SUBTASKS_REJECT_MINE
  )),
  VOLUNTEER("volunteer", Set.of(
      UserAuthority.CATEGORIES_VIEW,

      UserAuthority.STORES_VIEW_PUBLIC,

      UserAuthority.PRODUCTS_VIEW,

      UserAuthority.TASKS_VIEW,

      UserAuthority.SUBTASKS_VIEW,
      UserAuthority.SUBTASKS_MODIFY,
      UserAuthority.SUBTASKS_REJECT_MINE
  ));

  private final String value;
  private final Set<UserAuthority> authorities;

  UserRole(final String value, final Set<UserAuthority> authorities) {
    this.value = value;
    this.authorities = authorities;
  }

  public String getValue() {
    return value;
  }

  public Set<UserAuthority> getAuthorities() {
    return authorities;
  }

  public static UserRole forValue(final String source) {
    return Arrays.stream(UserRole.values())
        .filter(role -> role.getValue().equalsIgnoreCase(source))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(String.format(
            "User role '%s' is not supported", source)));
  }

}
