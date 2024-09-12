package com.mcoldibelli.authservice.domain.dto;

/**
 * A Data Transfer Object (DTO) representing authentication credentials.
 * <p>
 * This DTO is used to transfer user login and password information during the authentication
 * process.
 * </p>
 *
 * @param login    the login or username of the user attempting to authenticate
 * @param password the password of the user attempting to authenticate
 */
public record AuthenticationDto(String login, String password) {

}
