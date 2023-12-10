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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private static final String TAG = "GameController";
    private Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> fetchAllGames() {
        Map<String, List<Game>> response = Map.of("games", service.fetchAllGames());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createGame(@Valid @RequestBody Game game) {
        try {
            Game savedGame = service.createGame(game);
            if (savedGame != null) {
                return new ResponseEntity<>(savedGame, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(
                        new ErrorResponse(
                                "Error creating game",
                                ZonedDateTime.now().toString()
                        ),HttpStatus.BAD_REQUEST);
            }
        } catch (DataIntegrityViolationException e) {
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
}
