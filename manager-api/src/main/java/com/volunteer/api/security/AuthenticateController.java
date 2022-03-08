package com.volunteer.api.security;

import com.volunteer.api.security.model.AuthenticationRequest;
import com.volunteer.api.security.model.AuthenticationResponse;
import com.volunteer.api.security.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Lazy
@RestController
@RequestMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticateController {

  private static final Logger LOG = LoggerFactory.getLogger(AuthenticateController.class);

  private final AuthenticationManager authenticationManager;
  private final int serverPort;

  @Autowired
  public AuthenticateController(final AuthenticationManager authenticationManager,
      @LocalServerPort final int serverPort) {
    this.authenticationManager = authenticationManager;
    this.serverPort = serverPort;
  }

  @PostMapping
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request) {
    LOG.info("Logging in user '{}'", request.getUserName());

    final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        request.getUserName(), request.getPassword());
    final Authentication authentication = authenticationManager.authenticate(token);

    final User user = (User) authentication.getPrincipal();

    final AuthenticationResponse response = new AuthenticationResponse();
    response.setAccessToken(JwtUtils.generateAccessToken(user));
    response.setRefreshToken(JwtUtils.generateRefreshToken(user));

    return ResponseEntity.ok(response);
  }

}
