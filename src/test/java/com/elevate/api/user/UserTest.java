package com.elevate.api.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void testUserEntity() {
        String email = "test.user@elabs.com";
        String username = "testUser";
        String fullName = "Test User";
        String password = "pwd123test";

        User user = new User(email, username, fullName, password);

        assertEquals(user.getEmail(), "test.user@elabs.com");
        assertEquals(user.getUsername(), "testUser");
        assertEquals(user.getFullName(), "Test User");
        assertEquals(user.getPassword(), "pwd123test");

        String updatedEmail = "user.updated@elabs.com";
        user.setEmail(updatedEmail);
        assertEquals(user.getEmail(), updatedEmail);

        String updatedUsername = "updatedUserName";
        user.setUsername(updatedUsername);
        assertEquals(user.getUsername(), updatedUsername);

        String updatedFullName = "Full Name";
        user.setFullName(updatedFullName);
        assertEquals(user.getFullName(), updatedFullName);

        String updatedPassword = "newpwd#123";
        user.setPassword(updatedPassword);
        assertEquals(user.getPassword(), updatedPassword);

    }
}