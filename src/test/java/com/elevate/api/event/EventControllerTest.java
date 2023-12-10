package com.elevate.api.event;

import com.elevate.api.BaseControllerTest;
import com.elevate.api.exception.NotFoundException;
import com.elevate.api.game.Game;
import com.elevate.api.game.GameCategory;
import com.elevate.api.game.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerTest extends BaseControllerTest {

    @MockBean
    private EventService service;

    @MockBean
    private GameService gameService;

    @Test
    void createEventUnAuthorized() throws Exception {
        Event event = new Event("occurred", "12:00pm", new Game());
        given(service.createEvent(Mockito.any(Event.class))).willReturn(event);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/game_events")
                        .contentType("application/json")
                        .content("{\"game_event\": {\"type\": \"occurred\", \"occurred_at\": \"12:00pm\", \"game_id\": 1}}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createEventSuccess() throws Exception {
        long gameId = 1L;
        Game game = new Game("Name", "https://example.com", GameCategory.MATH);
        game.setId(gameId);

        Event event = new Event("occurred", "12:00pm", game);
        given(gameService.getGameById(gameId)).willReturn(game);
        given(service.createEvent(Mockito.any(Event.class))).willReturn(event);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/game_events").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                .contentType("application/json")
                .content("{\"game_event\": {\"type\": \"occurred\", \"occurred_at\": \"12:00pm\", \"game_id\": 1}}"))
                .andExpect(status().isCreated());
    }

    @Test
    void createEventWithUnknownGame() throws Exception {
        long gameId = 1L;
        Game game = new Game("Name", "https://example.com", GameCategory.MATH);
        game.setId(gameId);

        Event event = new Event("occurred", "12:00pm", game);
        given(gameService.getGameById(gameId)).willThrow(NotFoundException.class);
        given(service.createEvent(Mockito.any(Event.class))).willReturn(event);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/game_events").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                .contentType("application/json")
                .content("{\"game_event\": {\"type\": \"occurred\", \"occurred_at\": \"12:00pm\", \"game_id\": 1}}"))
                .andExpect(status().isNotFound());
    }
}