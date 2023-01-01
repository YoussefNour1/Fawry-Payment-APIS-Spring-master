package com.se.fawry.model.repository;

import com.se.fawry.enums.Role;
import com.se.fawry.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
    Optional<User> findByRole(Role role);
}