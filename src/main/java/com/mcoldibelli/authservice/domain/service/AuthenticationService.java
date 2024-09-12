package com.mcoldibelli.authservice.domain.service;

import com.mcoldibelli.authservice.domain.enums.UserRole;
import com.mcoldibelli.authservice.domain.model.CustomUserDetails;
import com.mcoldibelli.authservice.domain.model.User;
import com.mcoldibelli.authservice.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for managing user authentication and authorization.
 * <p>
 * This service implements {@link UserDetailsService} to integrate with Spring Security's
 * authentication mechanism. It provides methods to check for user existence, load user details for
 * authentication, and register new users.
 * </p>
 */
@Service
@AllArgsConstructor
public class AuthenticationService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public boolean userExists(String login) {
    return userRepository.findByLogin(login).isPresent();
  }

  @Override
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    User user = userRepository.findByLogin(login)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));

    return new CustomUserDetails(user);
  }

  /**
   * Registers a new user with the given login, password, and role.
   *
   * @param login    the login or username of the new user
   * @param password the raw password for the new user, which will be encoded
   * @param role     the role to assign to the new user (e.g., ADMIN or USER)
   */
  public void registerNewUser(String login, String password, UserRole role) {
    String encodedPassword = passwordEncoder.encode(password);
    User newUser = User.createUser(login, encodedPassword, role);
    userRepository.save(newUser);
  }
}
