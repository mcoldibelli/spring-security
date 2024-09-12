package com.mcoldibelli.authservice.domain.dto;

import com.mcoldibelli.authservice.domain.enums.UserRole;

/**
 * A Data Transfer Object representing user registration details.
 *
 * @param login    the login or username of the user to be registered
 * @param password the password for the new user account
 * @param role     the role assigned to the new user (e.g., ADMIN or USER)
 */
public record RegisterDto(String login, String password, UserRole role) {

}
