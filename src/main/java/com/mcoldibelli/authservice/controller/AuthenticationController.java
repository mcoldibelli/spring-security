package com.mcoldibelli.authservice.controller;

import com.mcoldibelli.authservice.domain.dto.RegisterDto;
import com.mcoldibelli.authservice.domain.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
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
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

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
}
