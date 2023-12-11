package com.elevate.api.user;

import com.elevate.api.exception.NotFoundException;
import com.elevate.api.statistics.UserStats;
import com.elevate.api.statistics.UserStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class UserService {
    private static final String TAG = "UserService";
    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserRepository repository;
    private final UserStatsRepository userStatsRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, UserStatsRepository userStatsRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userStatsRepository = userStatsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(User user) {
        if (emailExists(user.getEmail())) {
            logger.info(TAG + " - createUser: Email already exists for user " + user.getEmail());
            throw new DataIntegrityViolationException("Email already exists");
        }
        if (usernameExists(user.getUsername())) {
            logger.info(TAG + " - createUser: Username already exists for user " + user.getUsername());
            throw new DataIntegrityViolationException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = repository.save(user);

        UserStats userStats = new UserStats();
        userStats.setUser(savedUser);
        userStatsRepository.save(userStats);
        return savedUser;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.info(TAG + " - getCurrentUser: User is not authenticated");
            throw new NotFoundException("User is not authenticated");
        }
        String username = authentication.getName();
        return repository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email).isPresent();
    }

    private boolean usernameExists(String username) {
        return repository.findByUsername(username).isPresent();
    }
}
