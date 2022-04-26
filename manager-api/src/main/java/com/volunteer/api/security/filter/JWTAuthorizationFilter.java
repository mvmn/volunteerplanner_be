package com.volunteer.api.security.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.volunteer.api.service.JWTService;
import com.volunteer.api.utils.AuthenticationUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private final HandlerExceptionResolver exceptionResolver;

  @Override
  protected void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain filterChain)
      throws ServletException, IOException {
    try {
      handleAuthorizationHeader(request);
      filterChain.doFilter(request, response);
    } catch (final TokenExpiredException exception) {
      exceptionResolver.resolveException(request, response, null, exception);
    }
  }

  private void handleAuthorizationHeader(final HttpServletRequest request) {
    final Optional<String> token = AuthenticationUtils.getToken(request);
    if (token.isEmpty()) {
      return;
    }

    final DecodedJWT decodedToken = jwtService.validateAccessToken(token.get());

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
