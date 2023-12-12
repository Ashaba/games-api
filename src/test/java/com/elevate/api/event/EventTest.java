package com.elevate.api.event;

import com.elevate.api.game.Game;
import com.elevate.api.game.GameCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class EventTest {
    Event event;
    ZonedDateTime zonedDateTime;
    @BeforeEach
    void setUp() {
        String occurredAt = "2023-12-10T15:30:00+02:00";
        zonedDateTime = ZonedDateTime.parse(occurredAt, DateTimeFormatter.ISO_DATE_TIME);
        event = new Event("Game Started",occurredAt, null , zonedDateTime);
    }

    @Test
    void getType() {
        Assertions.assertEquals(event.getType(), "Game Started");
    }

    @Test
    void getOccurredAt() {
        Assertions.assertEquals(event.getOccurredAt(), "2023-12-10T15:30:00+02:00");
    }

    @Test
    void getGame() {
        Assertions.assertNull(event.getGame());
    }

    @Test
    void setType() {
        event.setType("Game Ended");
        Assertions.assertEquals(event.getType(), "Game Ended");
    }

    @Test
    void setOccurredAt() {
        String updatedOccurredAt = "2024-12-10T15:30:00+02:00";
        zonedDateTime = ZonedDateTime.parse(updatedOccurredAt, DateTimeFormatter.ISO_DATE_TIME);
        event.setOccurredAt(updatedOccurredAt);
        Assertions.assertEquals(event.getOccurredAt(), updatedOccurredAt);
    }

    @Test
    void setGame() {
        Game game = new Game("Math", "https://www.math.com", GameCategory.MATH);
        event.setGame(game);
        Assertions.assertEquals(event.getGame(), game);
    }
}