package com.volunteer.api.security.utils;

import com.volunteer.api.data.model.UserAuthority;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.Authentication;

public final class AuthenticationUtils {

  public static boolean hasAuthority(final UserAuthority authority, final Authentication auth) {
    return Optional.ofNullable(auth.getAuthorities()).orElseGet(Collections::emptyList).stream()
        .anyMatch(item -> item.getAuthority().equals(authority.name()));
  }

  private AuthenticationUtils() {
    // empty constructor
  }

}
