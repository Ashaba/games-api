package com.elevate.api.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
    Game game;

    @BeforeEach
    void setUp() {
        game = new Game( "Math", "https://www.math.com", GameCategory.MATH);
    }

    @Test
    void getName() {
        Assertions.assertEquals(game.getName(), "Math");
    }

    @Test
    void getUrl() {
        Assertions.assertEquals(game.getUrl(), "https://www.math.com");
    }

    @Test
    void getCategory() {
        Assertions.assertEquals(game.getCategory(), GameCategory.MATH);
    }

    @Test
    void setName() {
        game.setName("Reading");
        Assertions.assertEquals(game.getName(), "Reading");
    }

    @Test
    void setUrl() {
        game.setUrl("https://www.reading.com");
        Assertions.assertEquals(game.getUrl(), "https://www.reading.com");
    }

    @Test
    void setCategory() {
        game.setCategory(GameCategory.READING);
        Assertions.assertEquals(game.getCategory(), GameCategory.READING);
    }
}