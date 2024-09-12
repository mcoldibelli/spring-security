package com.mcoldibelli.authservice.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mcoldibelli.authservice.domain.model.CustomUserDetails;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling JWT token generation and validation.
 * <p>
 * This service uses the Auth0 JWT library to create and verify tokens for user authentication.
 * </p>
 */
@Service
public class TokenService {

  private static final String ISSUER = "auth-mcoldibelli";

  @Value("${api.security.token.secret}")
  private String secret;

  @Value("${api.security.token.expiration-minutes}")
  private long expirationMinutes;

  /**
   * Generates a JWT token for the given user details.
   */
  public String generateToken(CustomUserDetails userDetails) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
          .withIssuer(ISSUER)
          .withSubject(userDetails.getUsername())
          .withExpiresAt(generateExpirationDate())
          .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Could not generate token", exception);
    }
  }

  /**
   * Validates the given JWT token and retrieves the subject (username/login).
   *
   * @param token the JWT token to validate
   */
  public Optional<String> validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return Optional.ofNullable(
          JWT.require(algorithm)
              .withIssuer(ISSUER)
              .build()
              .verify(token)
              .getSubject());
    } catch (JWTVerificationException exception) {
      return Optional.empty();
    }
  }

  /**
   * Generates the token expiration date based on the current time and the configured expiration
   * time.
   */
  private Instant generateExpirationDate() {
    return LocalDateTime.now().plusMinutes(expirationMinutes).toInstant(ZoneOffset.UTC);
  }

}
