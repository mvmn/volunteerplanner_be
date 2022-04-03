package com.volunteer.api.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.volunteer.api.service.JWTService;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

  private static final String HEADER_VALUE_PREFIX = "Bearer";

  private final JWTService jwtService;

  @Override
  protected void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain filterChain)
      throws ServletException, IOException {
    handleAuthorizationHeader(request);
    filterChain.doFilter(request, response);
  }

  private void handleAuthorizationHeader(final HttpServletRequest request) {
    final String headerValue = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (!(StringUtils.hasLength(headerValue) && headerValue.startsWith(HEADER_VALUE_PREFIX))) {
      return;
    }

    final String token = headerValue.substring(HEADER_VALUE_PREFIX.length()).trim();
    final DecodedJWT decodedToken = jwtService.validateAccessToken(token);

    final String principal = jwtService.getPrincipal(decodedToken);
    final Collection<String> roles = jwtService.getRoles(decodedToken);

    final Collection<SimpleGrantedAuthority> authorities = roles.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(principal, null, authorities);
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

}
