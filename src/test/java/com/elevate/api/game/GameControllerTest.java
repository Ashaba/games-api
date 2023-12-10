package com.elevate.api.game;

import com.elevate.api.BaseControllerTest;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GameControllerTest extends BaseControllerTest {

    @MockBean
    private GameService gameService;

    @Test
    void fetchAllGamesUnAuthorized() throws Exception {
        Game game1 = new Game("Math", "https://www.math.com", GameCategory.MATH);
        Game game2 = new Game("Science", "https://www.science.com", GameCategory.READING);

        List<Game> games = Arrays.asList(game1, game2);
        given(gameService.fetchAllGames()).willReturn(games);

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void fetchAllGamesSuccess() throws Exception {

        Game game1 = new Game("Math", "https://www.math.com", GameCategory.MATH);
        Game game2 = new Game("Science", "https://www.science.com", GameCategory.READING);

        List<Game> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);
        given(gameService.fetchAllGames()).willReturn(games);

        mockMvc.perform(get("/api/games").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.games.size()").value(games.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.games[0].name").value(game1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.games[1].url").value(game2.getUrl()));
    }

    @Test
    @WithMockUser
    void fetchAllGamesNoGames() throws Exception {
        List<Game> games = new ArrayList<>();
        given(gameService.fetchAllGames()).willReturn(games);

        mockMvc.perform(get("/api/games").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.games.size()", Is.is(0)));
    }

    @Test
    void createGameSuccess() throws Exception {
        Game game = new Game("Math", "https://www.math.com", GameCategory.MATH);
        given(gameService.createGame(any(Game.class))).willReturn(game);

        mockMvc.perform(post("/api/games").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(game)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Math")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", Is.is("https://www.math.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", Is.is("MATH")));
    }

    @Test
    void createGameInvalidUrl() throws Exception {
        Game game = new Game("Math", "invalid url", GameCategory.MATH);
        given(gameService.createGame(any(Game.class))).willReturn(game);

        mockMvc.perform(post("/api/games").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(game)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", Is.is("Invalid URL")));
    }

    @Test
    void createGameWithNoParameters() throws Exception {
        Game game = new Game();
        given(gameService.createGame(any(Game.class))).willReturn(game);

        mockMvc.perform(post("/api/games").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(game)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Name is required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", Is.is("URL is required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", Is.is("Category is required")));
    }
}