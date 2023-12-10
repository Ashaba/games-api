package com.elevate.api.game;

import com.elevate.api.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameRepository repository;

    @Autowired
    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    public Game createGame(Game game) {
        try {
            return repository.save(game);
        } catch (Exception e) {
            logger.info("Error creating game: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<Game> fetchAllGames() {
        return repository.findAll();
    }

    public Game getGameById(Long id) {
        Optional<Game> optionalGame = repository.findById(id);
        return optionalGame.orElseThrow(() -> new NotFoundException("Game not found"));
    }
}
