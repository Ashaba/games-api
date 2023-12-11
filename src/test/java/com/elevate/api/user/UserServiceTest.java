package com.elevate.api.user;

import com.elevate.api.exception.NotFoundException;
import com.elevate.api.statistics.UserStatsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Mock
    private UserStatsRepository userStatsRepository;

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

    @Test
    void getCurrentUserAuthenticatedAndFound() {
        User expectedUser = new User("test.email@elab.com", "testUsername", "User Test", "password");
        User actualUser;

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Authentication authentication = Mockito.mock(Authentication.class);

            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn("testUsername");

            when(repository.findByUsername("testUsername")).thenReturn(Optional.of(expectedUser));

            actualUser = userService.getCurrentUser();
        }

        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser.getUsername(), actualUser.getUsername());
    }

    @Test
    void getCurrentUserNotAuthenticated() {
        Exception exception = null;

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Authentication authentication = Mockito.mock(Authentication.class);

            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(false);

            userService.getCurrentUser();
        } catch (NotFoundException e) {
            exception = e;
        }

        Assertions.assertNotNull(exception);
    }


    @Test
    void getCurrentUserAuthenticatedButNotFound() {
        Exception exception = null;

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Authentication authentication = Mockito.mock(Authentication.class);

            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn("unknownUser");

            when(repository.findByUsername("unknownUser")).thenReturn(Optional.empty());

            userService.getCurrentUser();
        } catch (NotFoundException e) {
            exception = e;
        }

        Assertions.assertNotNull(exception, "Expected NotFoundException to be thrown");
    }

}
