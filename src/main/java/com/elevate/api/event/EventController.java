package com.elevate.api.event;

import com.elevate.api.game.Game;
import com.elevate.api.game.GameService;
import com.elevate.api.user.User;
import com.elevate.api.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/user/game_events")
public class EventController {

    private final EventService eventService;
    private final GameService gameService;
    private final UserService userService;

    @Autowired
    public EventController(EventService eventService, GameService gameService, UserService userService) {
        this.eventService = eventService;
        this.gameService = gameService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Map<String, EventDTO> payload) {
        EventDTO eventDTO = payload.get("game_event");
        Game game = gameService.getGameById(eventDTO.getGameId());

        Event event = new Event(eventDTO.getType(), eventDTO.getOccurredAt(), game, ZonedDateTime.now());
        User currentUser = userService.getCurrentUser();
        event.setUser(currentUser);
        return new ResponseEntity<>(eventService.createEvent(event), HttpStatus.CREATED);
    }
}
