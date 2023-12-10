package com.elevate.api.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository repository;

    @InjectMocks
    private GameService service;

    @Test
    void createGameSuccess() {
        Game game = new Game("Math", "https://www.math.com", GameCategory.MATH);
        when(repository.save(Mockito.any(Game.class))).thenReturn(game);

        Game createdGame = service.createGame(game);

        Assertions.assertNotNull(createdGame);
        Assertions.assertEquals("Math", createdGame.getName());
        Assertions.assertEquals("https://www.math.com", createdGame.getUrl());
        Assertions.assertEquals(GameCategory.MATH, createdGame.getCategory());
        verify(repository).save(game);
    }

    @Test
    void createGameFailure() {
        Game game = new Game("Math", "https://www.math.com", GameCategory.MATH);
        when(repository.save(Mockito.any(Game.class))).thenReturn(null);

        Game createdGame = service.createGame(game);

        Assertions.assertNull(createdGame);
        verify(repository).save(game);
    }

    @Test
    void fetchAllGames() {
        List<Game> games = new ArrayList<>();
        Assertions.assertEquals(0, games.size());

        when(repository.findAll()).thenReturn(games);
        Game game1 = new Game("Math", "https://www.math.com", GameCategory.MATH);
        Game game2 = new Game("Reading", "https://www.reading.com", GameCategory.READING);
        when(repository.findAll()).thenReturn(Arrays.asList(game1, game2));

        games = service.fetchAllGames();

        Assertions.assertNotNull(games);
        Assertions.assertEquals(2, games.size());
        verify(repository).findAll();
    }
}