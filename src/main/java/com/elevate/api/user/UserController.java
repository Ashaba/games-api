package com.elevate.api.user;

import com.elevate.api.auth.AuthService;
import com.elevate.api.auth.AuthTokenResponse;
import com.elevate.api.auth.Credential;
import com.elevate.api.statistics.UserStats;
import com.elevate.api.statistics.UserStatsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final String TAG = "UserController";
    private final Logger logger = Logger.getLogger(UserController.class.getName());
    private final UserService userService;
    private final AuthService authService;
    private final UserStatsService userStatsService;

    @Autowired
    public UserController(UserService userService, AuthService authService, UserStatsService userStatsService) {
        this.userService = userService;
        this.authService = authService;
        this.userStatsService = userStatsService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        logger.info(TAG + " - Creating user: " + user.getEmail());
        try {
            User savedUser = userService.createUser(user);
            if (savedUser != null) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (DataIntegrityViolationException e) {
            logger.info(TAG + " " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/sessions")
    public ResponseEntity<?> authenticateUser(@RequestBody Credential credential) {
        String token = authService.Authenticate(credential);
        logger.info(TAG + " - Successfully retrieved token");
        return ResponseEntity.ok(new AuthTokenResponse(token));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails() {
        User user = userService.getCurrentUser();
        UserStats userStats = userStatsService.getUserStatsForUser(user.getId());

        logger.info(TAG + " - Successfully retrieved user details");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setFullName(user.getFullName());
        userResponse.setUserStats(userStats);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
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
