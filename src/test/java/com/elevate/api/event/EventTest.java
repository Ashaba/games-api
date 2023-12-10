package com.elevate.api.event;

import com.elevate.api.game.Game;
import com.elevate.api.game.GameCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventTest {
    Event event;

    @BeforeEach
    void setUp() {
        event = new Event("Game Started", "2021-09-01 12:00:00", null);
    }

    @Test
    void getType() {
        Assertions.assertEquals(event.getType(), "Game Started");
    }

    @Test
    void getOccurredAt() {
        Assertions.assertEquals(event.getOccurredAt(), "2021-09-01 12:00:00");
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
        event.setOccurredAt("2021-09-01 12:00:01");
        Assertions.assertEquals(event.getOccurredAt(), "2021-09-01 12:00:01");
    }

    @Test
    void setGame() {
        Game game = new Game("Math", "https://www.math.com", GameCategory.MATH);
        event.setGame(game);
        Assertions.assertEquals(event.getGame(), game);
    }
}