package com.mcoldibelli.springsec.repository;

import com.mcoldibelli.springsec.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User Repository.
 */
public interface UserRepository extends JpaRepository<User, String> {

}
