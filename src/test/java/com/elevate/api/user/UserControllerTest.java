package com.elevate.api.user;

import com.elevate.api.BaseControllerTest;
import com.elevate.api.auth.AuthService;
import com.elevate.api.auth.Credential;
import com.elevate.api.statistics.UserStats;
import com.elevate.api.statistics.UserStatsService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @Mock
    private UserStatsService userStatsService;

    @Test
    void createUserSuccess() throws Exception {
        User user = new User("test@email.com", "testUsername", "Test User", "password");
        given(userService.createUser(any(User.class))).willReturn(user);

        mockMvc.perform(post("/api/user")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    void createUserWithoutRequiredFields() throws Exception {
        User user = new User();
        given(userService.createUser(any(User.class))).willReturn(user);

        mockMvc.perform(post("/api/user")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is("Email is required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Is.is("Username is required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", Is.is("full_name is required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", Is.is("Password is required")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithInvalidInput() throws Exception {
        User user = new User("invalid email", "testUsername", "Test User", "password");
        given(userService.createUser(any(User.class))).willReturn(user);

        mockMvc.perform(post("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is("Invalid Email Address")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithExistingEmail() throws Exception {
        User user = new User("existing@email.com", "testUsername", "Test User", "password");

        given(userService.createUser(any(User.class)))
                .willThrow(new DataIntegrityViolationException("Email already exists"));

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Is.is("Email already exists")))
                .andExpect(status().isConflict());
    }

    @Test
    void createUserWithExistingUsername() throws Exception {
        User user = new User("test@email.com", "existingUser", "Test User", "password");

        given(userService.createUser(any(User.class)))
                .willThrow(new DataIntegrityViolationException("Username already exists"));

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Is.is("Username already exists")))
                .andExpect(status().isConflict());
    }

    @Test
    void AuthenticateUserSuccess() throws Exception {
        Credential credential = new Credential("username", "password");
        given(authService.Authenticate(any(Credential.class))).willReturn("token");

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credential)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", Is.is("token")))
                .andExpect(status().isOk());
    }

    @Test
    void AuthenticateUserWithInvalidCredentials() throws Exception {
        Credential credential = new Credential("invalid", "password");
        given(authService.Authenticate(any(Credential.class)))
                .willThrow(new BadCredentialsException("Invalid Credentials"));

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credential)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserDetailsSuccess() throws Exception {
        User user = new User("test@email.com", "testUsername", "Test User", "password");
        user.setId(1L);
        UserStats userStats = new UserStats();

        given(userService.getCurrentUser()).willReturn(user);
        given(userStatsService.getUserStatsForUser(user.getId())).willReturn(userStats);

        mockMvc.perform(get("/api/user").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is(user.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Is.is(user.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.full_name", Is.is(user.getFullName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stats.total_games_played", Is.is(userStats.getTotalGamesPlayed())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stats.total_math_games_played", Is.is(userStats.getTotalMathGamesPlayed())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stats.total_reading_games_played", Is.is(userStats.getTotalReadingGamesPlayed())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stats.total_speaking_games_played", Is.is(userStats.getTotalSpeakingGamesPlayed())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stats.total_writing_games_played", Is.is(userStats.getTotalWritingGamesPlayed())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stats.current_streak_in_days", Is.is(userStats.getCurrentStreakInDays())))
                .andExpect(status().isOk());
    }

    @Test
    void getUserDetailsWithoutToken() throws Exception {
        mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
