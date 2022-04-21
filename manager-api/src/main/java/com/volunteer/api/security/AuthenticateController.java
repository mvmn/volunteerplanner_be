package com.volunteer.api.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.volunteer.api.security.model.AuthenticationRequest;
import com.volunteer.api.security.model.AuthenticationResponse;
import com.volunteer.api.service.JWTService;
import com.volunteer.api.utils.AuthenticationUtils;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Lazy
@Validated
@RestController
@RequestMapping(value = "/api/v1/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthenticateController {

  private static final Logger LOG = LoggerFactory.getLogger(AuthenticateController.class);

  private final AuthenticationManager authenticationManager;

  private final JWTService jwtService;
  private final UserDetailsService userService;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public AuthenticationResponse authenticate(@RequestBody @NotNull AuthenticationRequest request) {
    LOG.info("Logging in user '{}'", request.getPrincipal());

    final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        request.getPrincipal(), request.getPassword());
    final Authentication authentication = authenticationManager.authenticate(token);

    final User user = (User) authentication.getPrincipal();

    final AuthenticationResponse response = new AuthenticationResponse();
    response.setAccessToken(jwtService.generateAccessToken(user));
    response.setRefreshToken(jwtService.generateRefreshToken(user));

    return response;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public AuthenticationResponse authenticate(@NotNull final HttpServletRequest request) {
    final Optional<String> token = AuthenticationUtils.getToken(request);
    if (token.isEmpty()) {
      throw new AuthenticationServiceException("Refresh token is missed");
    }

    final DecodedJWT decodedToken = jwtService.validateRefreshToken(token.get());
    final String principal = jwtService.getPrincipal(decodedToken);

    LOG.info("Refreshing token for '{}'", principal);
    final User user = (User) userService.loadUserByUsername(principal);

    final AuthenticationResponse response = new AuthenticationResponse();
    response.setAccessToken(jwtService.generateAccessToken(user));
    response.setRefreshToken(jwtService.generateRefreshToken(user));

    return response;
  }

}
