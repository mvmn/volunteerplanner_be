package com.volunteer.api.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Collection;
import org.springframework.security.core.userdetails.User;

public interface JWTService {

  String generateAccessToken(final User user);

  DecodedJWT validateAccessToken(final String token);

  String generateRefreshToken(final User user);

  DecodedJWT validateRefreshToken(final String token);

  String getPrincipal(final DecodedJWT decodedToken);

  Collection<String> getRoles(final DecodedJWT decodedToken);

}
