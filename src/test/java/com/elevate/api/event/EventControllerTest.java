package com.elevate.api.event;

import com.elevate.api.BaseControllerTest;
import com.elevate.api.exception.NotFoundException;
import com.elevate.api.game.Game;
import com.elevate.api.game.GameCategory;
import com.elevate.api.game.GameService;
import com.elevate.api.statistics.UserStatsService;
import com.elevate.api.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerTest extends BaseControllerTest {

    @MockBean
    private EventService service;
    @MockBean
    private GameService gameService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserStatsService userStatsService;

    @Test
    void createEventUnAuthorized() throws Exception {
        String occurredAt = "2023-12-10T15:30:00+02:00";
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(occurredAt, DateTimeFormatter.ISO_DATE_TIME);

        Event event = new Event("occurred", occurredAt, new Game(), zonedDateTime);
        given(service.createEvent(Mockito.any(Event.class))).willReturn(event);

        String jsonPayload = String.format("{\"game_event\": {\"type\": \"occurred\", \"occurred_at\": \"%s\", \"game_id\": 1}}", occurredAt);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/game_events")
                        .contentType("application/json")
                        .content(jsonPayload))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createEventSuccess() throws Exception {
        long gameId = 1L;
        Game game = new Game("Name", "https://example.com", GameCategory.MATH);
        game.setId(gameId);

        String occurredAt = "2023-12-10T15:30:00+02:00";
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(occurredAt, DateTimeFormatter.ISO_DATE_TIME);

        Event event = new Event("occurred", occurredAt, new Game(), zonedDateTime);
        given(gameService.getGameById(gameId)).willReturn(game);
        given(service.createEvent(Mockito.any(Event.class))).willReturn(event);

        String jsonPayload = String.format("{\"game_event\": {\"type\": \"occurred\", \"occurred_at\": \"%s\", \"game_id\": 1}}", occurredAt);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/game_events").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                .contentType("application/json")
                .content(jsonPayload))
                .andExpect(status().isCreated());
    }

    @Test
    void createEventWithUnknownGame() throws Exception {
        long gameId = 1L;
        Game game = new Game("Name", "https://example.com", GameCategory.MATH);
        game.setId(gameId);

        String occurredAt = "2023-12-10T15:30:00+02:00";
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(occurredAt, DateTimeFormatter.ISO_DATE_TIME);

        Event event = new Event("occurred", occurredAt, new Game(), zonedDateTime);
        given(gameService.getGameById(gameId)).willThrow(NotFoundException.class);
        given(service.createEvent(Mockito.any(Event.class))).willReturn(event);

        String jsonPayload = String.format("{\"game_event\": {\"type\": \"occurred\", \"occurred_at\": \"%s\", \"game_id\": 1}}", occurredAt);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/game_events").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                .contentType("application/json")
                .content(jsonPayload))
                .andExpect(status().isNotFound());
    }
}
