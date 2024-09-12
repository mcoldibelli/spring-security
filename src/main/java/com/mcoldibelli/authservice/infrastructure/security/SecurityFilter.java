package com.mcoldibelli.authservice.infrastructure.security;

import com.mcoldibelli.authservice.domain.model.CustomUserDetails;
import com.mcoldibelli.authservice.domain.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter responsible for authenticating requests based on JWT tokens.
 */
@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

  private static final String AUTH_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

  private final UserRepository userRepository;
  private final TokenService tokenService;

  /**
   * Filters incoming requests and checks if the token is valid. If valid, it sets the
   * authentication in the {@link SecurityContextHolder}.
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    final String token = this.extractToken(request);

    if (token != null) {
      tokenService.validateToken(token).ifPresent(login -> {
        Optional<UserDetails> userDetailsOpt = userRepository.findByLogin(login)
            .map(CustomUserDetails::new);
        userDetailsOpt.ifPresent(this::setAuthentication);
      });
    }
    filterChain.doFilter(request, response);
  }

  /**
   * Extracts the token from the Authorization header.
   *
   * @param request the current HTTP request
   * @return the extracted JWT token, or {@code null} if the header is missing or invalid
   */
  private String extractToken(HttpServletRequest request) {
    final String authHeader = request.getHeader(AUTH_HEADER);

    if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
      return authHeader.substring(BEARER_PREFIX.length());
    }
    return null;
  }

  /**
   * Sets the authentication in the {@link SecurityContextHolder}.
   */
  private void setAuthentication(UserDetails userDetails) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }
}
