package com.mcoldibelli.authservice.repository;

import com.mcoldibelli.authservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User Repository.
 */
public interface UserRepository extends JpaRepository<User, String> {

}
