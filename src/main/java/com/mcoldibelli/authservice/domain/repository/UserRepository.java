package com.mcoldibelli.authservice.domain.repository;

import com.mcoldibelli.authservice.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User Repository.
 */
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByLogin(String login);
}
