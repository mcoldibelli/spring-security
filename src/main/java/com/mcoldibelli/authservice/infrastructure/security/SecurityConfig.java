package com.mcoldibelli.authservice.infrastructure.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for setting up authentication and authorization policies.
 * <p>
 * This configuration class defines a {@link SecurityFilterChain} to handle security-related
 * configurations, including token-based authentication using JWT, stateless session management, and
 * role-based access control.
 * </p>
 */
@Configuration
@EnableWebSecurity
@Getter
public class SecurityConfig {

  private static final String[] PUBLIC_POST_ENDPOINTS = {
      "/auth/register",
      "/auth/login"
  };

  private final SecurityFilter jwtAuthenticationFilter;

  @Autowired
  public SecurityConfig(SecurityFilter jwtAuthenticationFilter) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  /**
   * Configures the security filter chain for the application.
   * <p>
   * This method disables CSRF protection (as the app uses token-based authentication), enforces
   * stateless sessions, and sets up authorization rules. JWT-based authentication is handled by the
   * {@link SecurityFilter}.
   * </p>
   *
   * @param http the {@link HttpSecurity} object used to configure security
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  /**
   * Provides the {@link AuthenticationManager} bean used for authentication management.
   *
   * @param authenticationConfiguration the {@link AuthenticationConfiguration} used to build the
   *                                    manager
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * Provides the {@link PasswordEncoder} bean used for encoding passwords. Uses BCrypt hashing
   * algorithm.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
