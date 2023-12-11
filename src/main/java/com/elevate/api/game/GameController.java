package com.elevate.api.game;

import com.elevate.api.exception.ErrorResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private static final String TAG = "GameController";
    private final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> fetchAllGames() {
        Map<String, List<Game>> response = Map.of("games", service.fetchAllGames());
        logger.info(TAG + " - fetchAllGames: " + response.get("games").size() + " games found");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createGame(@Valid @RequestBody Game game) {
        if (!validateCategory(game.getCategory().name())) {
            logger.error(TAG + " - createGame: Invalid category " + game.getCategory().name());
            return new ResponseEntity<>(
                    new ErrorResponse(
                            "Invalid category, must be one of: " + Arrays.toString(GameCategory.values()),
                            ZonedDateTime.now().toString()
                    ),HttpStatus.BAD_REQUEST);
        }
        try {
            Game savedGame = service.createGame(game);
            logger.info(TAG + " - createGame: Created game with id " + savedGame.getId() );
            return new ResponseEntity<>(savedGame, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            logger.error(TAG + " - createGame: " + e.getMessage());
            return new ResponseEntity<>(
                    new ErrorResponse(
                            e.getMessage(),
                            ZonedDateTime.now().toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    /**
     * Adding this validator here as a workaround for the issue with my custom validator @ValueOfEnum annotation
     * which doesn't seem to do what it's supposed to do. Needs to be investigated.
     */
    public boolean validateCategory(String category) {
        for (GameCategory c : GameCategory.values()) {
            if (c.name().equals(category)) {
                return true;
            }
        }
        return false;
    }
}
