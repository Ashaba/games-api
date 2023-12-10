package com.elevate.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(User user) {
        if (emailExists(user.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        if (usernameExists(user.getUsername())) {
            throw new DataIntegrityViolationException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email).isPresent();
    }

    private boolean usernameExists(String username) {
        return repository.findByUsername(username).isPresent();
    }
}
