package com.volunteer.api.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public final class JwtUtils {

  private static final String SECRET = "secret";
  private static final Algorithm ALGORITHM = Algorithm.HMAC256(
      SECRET.getBytes(StandardCharsets.UTF_8));

  private static final long ACCESS_TOKEN_TTL = TimeUnit.MINUTES.toMillis(1);
  private static final long REFRESH_TOKEN_TTL = TimeUnit.HOURS.toMillis(12);

  private static final String CLAIM_ROLE = "role";

  public static String generateAccessToken(final User user) {
    return JwtUtils.generateToken(user, ACCESS_TOKEN_TTL);
  }

  public static DecodedJWT validateAccessToken(final String token) {
    return JwtUtils.validate(token, ACCESS_TOKEN_TTL);
  }

  public static String generateRefreshToken(final User user) {
    return JwtUtils.generateToken(user, REFRESH_TOKEN_TTL);
  }

  public static DecodedJWT validateRefreshToken(final String token) {
    return JwtUtils.validate(token, REFRESH_TOKEN_TTL);
  }

  public static String getUserName(final DecodedJWT decodedToken) {
    return decodedToken.getSubject();
  }

  public static Collection<String> getRoles(final DecodedJWT decodedToken) {
    return decodedToken.getClaim(CLAIM_ROLE).asList(String.class);
  }

  private static DecodedJWT validate(final String token, final long tokenTtl) {
    final JWTVerifier verifier = JWT.require(ALGORITHM)
        .acceptExpiresAt(TimeUnit.MILLISECONDS.toSeconds(tokenTtl))
        .build();

    return verifier.verify(token);
  }

  private static String generateToken(final User user, final long tokenTtl) {
    return JWT.create()
        .withSubject(user.getUsername())
        .withIssuedAt(new Date(System.currentTimeMillis()))
        .withExpiresAt(new Date(System.currentTimeMillis() + tokenTtl))
        .withClaim(CLAIM_ROLE, user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()))
        .sign(ALGORITHM);
  }


}
