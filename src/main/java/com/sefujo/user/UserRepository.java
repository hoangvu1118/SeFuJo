package com.sefujo.user;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(@NotBlank(message = "Email cannot be null") String email);

    boolean existsByEmail(String email);
}
