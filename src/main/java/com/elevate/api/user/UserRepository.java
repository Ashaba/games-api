package com.elevate.api.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Override
    Optional<User> findById(UUID uuid);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
