package com.volunteer.api.utils;

import com.volunteer.api.data.model.UserAuthority;
import java.util.Collections;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;

@UtilityClass
public final class AuthenticationUtils {

  public static boolean hasAuthority(final UserAuthority authority, final Authentication auth) {
    return Optional.ofNullable(auth.getAuthorities()).orElseGet(Collections::emptyList).stream()
        .anyMatch(item -> item.getAuthority().equals(authority.name()));
  }

}
