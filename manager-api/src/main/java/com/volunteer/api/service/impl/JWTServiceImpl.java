package com.volunteer.api.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.volunteer.api.service.JWTService;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public final class JWTServiceImpl implements JWTService {

  private static final String CLAIM_ROLE = "role";

  private final Algorithm algorithm;

  private final Duration accessTokenTtl;
  private final Duration refreshTokenTtl;

  public JWTServiceImpl(final String secret, final Duration accessTokenTtl,
      final Duration refreshTokenTtl) {
    this.algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

    this.accessTokenTtl = accessTokenTtl;
    this.refreshTokenTtl = refreshTokenTtl;
  }

  @Override
  public String generateAccessToken(final User user) {
    return generateToken(user, accessTokenTtl);
  }

  @Override
  public DecodedJWT validateAccessToken(final String token) {
    return validate(token, accessTokenTtl);
  }

  @Override
  public String generateRefreshToken(final User user) {
    return generateToken(user, refreshTokenTtl);
  }

  @Override
  public DecodedJWT validateRefreshToken(final String token) {
    return validate(token, refreshTokenTtl);
  }

  @Override
  public String getPrincipal(final DecodedJWT decodedToken) {
    return decodedToken.getSubject();
  }

  @Override
  public Collection<String> getRoles(final DecodedJWT decodedToken) {
    return decodedToken.getClaim(CLAIM_ROLE).asList(String.class);
  }

  private DecodedJWT validate(final String token, final Duration tokenTtl) {
    final JWTVerifier verifier = JWT.require(algorithm)
        .acceptExpiresAt(TimeUnit.SECONDS.convert(tokenTtl))
        .build();

    return verifier.verify(token);
  }

  private String generateToken(final User user, final Duration tokenTtl) {
    final long currentTime = System.currentTimeMillis();

    return JWT.create()
        .withSubject(user.getUsername())
        .withIssuedAt(new Date(currentTime))
        .withExpiresAt(new Date(currentTime + TimeUnit.MILLISECONDS.convert(tokenTtl)))
        .withClaim(CLAIM_ROLE, user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()))
        .sign(algorithm);
  }

}
