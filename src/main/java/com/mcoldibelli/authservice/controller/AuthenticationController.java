package com.mcoldibelli.authservice.controller;

import com.mcoldibelli.authservice.domain.dto.AuthenticationDto;
import com.mcoldibelli.authservice.domain.dto.LoginResponseDto;
import com.mcoldibelli.authservice.domain.dto.RegisterDto;
import com.mcoldibelli.authservice.domain.model.CustomUserDetails;
import com.mcoldibelli.authservice.domain.service.AuthenticationService;
import com.mcoldibelli.authservice.infrastructure.security.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for handling authentication-related requests.
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final AuthenticationService authenticationService;
  private final TokenService tokenService;

  /**
   * Handles the registration of a new user.
   *
   * @param data the registration details including login, password, and user role
   */
  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Validated RegisterDto data) {
    boolean userExists = authenticationService.userExists(data.login());

    if (userExists) {
      return ResponseEntity.badRequest().build();
    }

    authenticationService.registerNewUser(data.login(), data.password(), data.role());
    return ResponseEntity.ok().build();
  }

  /**
   * Handles user login and token generation.
   * <p>
   * This method authenticates the user using the provided login credentials. If the authentication
   * is successful, it generates a JWT token for the user and returns it in the response.
   * </p>
   *
   * @param data the login credentials including login and password
   */
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody @Validated AuthenticationDto data) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
    Authentication authentication = this.authenticationManager.authenticate(usernamePassword);

    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    var token = tokenService.generateToken(userDetails);

    return ResponseEntity.ok(new LoginResponseDto(token));
  }
}
