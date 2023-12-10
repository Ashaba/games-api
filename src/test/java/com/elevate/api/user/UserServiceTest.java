package com.elevate.api.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void createUserSuccess() {
        String rawPassword = "password123";
        User user = new User("test.email@elab.com", "testUsername", "User Test", rawPassword);

        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");

        userService.createUser(user);

        verify(repository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();


        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(savedUser.getPassword(), "encodedPassword");
        Assertions.assertEquals(savedUser.getEmail(), user.getEmail());
        Assertions.assertEquals(savedUser.getUsername(), user.getUsername());
        Assertions.assertEquals(savedUser.getFullName(), user.getFullName());
        Assertions.assertNotEquals(savedUser.getPassword(), rawPassword);
    }

    @Test
    void createUserWithDuplicateEmail() {
        String rawPassword = "password123";
        User user = new User("duplicate.email@elab.com", "newUsername", "User Test", rawPassword);

        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userService.createUser(user);
        }, "Should throw DataIntegrityViolationException for duplicate email");
    }

    @Test
    void createUserWithDuplicateUsername() {
        String rawPassword = "password123";
        User user = new User("new.email@elab.com", "duplicateUsername", "User Test", rawPassword);

        when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userService.createUser(user);
        }, "Should throw DataIntegrityViolationException for duplicate username");
    }
}
