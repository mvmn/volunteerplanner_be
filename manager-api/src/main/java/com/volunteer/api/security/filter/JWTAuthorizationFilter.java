package com.volunteer.api.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.volunteer.api.service.JWTService;
import com.volunteer.api.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private final HandlerExceptionResolver exceptionResolver;
  private final boolean enableCors;

  @Override
  protected void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain filterChain)
      throws ServletException, IOException {
    try {
      if (!enableCors || !request.getMethod().equalsIgnoreCase("OPTIONS")) {
        handleAuthorizationHeader(request);
      }
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

    try {
      final DecodedJWT decodedToken = jwtService.validateAccessToken(token.get());

      final String principal = jwtService.getPrincipal(decodedToken);
      final Collection<String> roles = jwtService.getRoles(decodedToken);

      final Collection<SimpleGrantedAuthority> authorities =
          roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

      final UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(principal, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    } catch (JWTVerificationException sve) {
      return;
    }
  }

}
