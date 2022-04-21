package com.volunteer.api.utils;

import com.volunteer.api.data.model.UserAuthority;
import java.util.Collections;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

@UtilityClass
public final class AuthenticationUtils {

  private static final String HEADER_VALUE_PREFIX = "Bearer";

  public static Optional<String> getToken(final HttpServletRequest request) {
    final String headerValue = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (!(StringUtils.isNotEmpty(headerValue) && headerValue.startsWith(HEADER_VALUE_PREFIX))) {
      return Optional.empty();
    }

    return Optional.of(headerValue.substring(HEADER_VALUE_PREFIX.length()).strip());
  }

  public static boolean hasAuthority(final UserAuthority authority, final Authentication auth) {
    return Optional.ofNullable(auth.getAuthorities()).orElseGet(Collections::emptyList).stream()
        .anyMatch(item -> item.getAuthority().equals(authority.name()));
  }

}
