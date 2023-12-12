package com.elevate.api.statistics;

import com.elevate.api.event.Event;
import com.elevate.api.event.EventRepository;
import com.elevate.api.game.Game;
import com.elevate.api.game.GameCategory;
import com.elevate.api.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStatsServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserStatsRepository userStatsRepository;

    @InjectMocks
    private UserStatsService userStatsService;

    @Test
    void getUserStatsForUserShouldReturnStats() {
        Long userId = 1L;
        UserStats mockedUserStats = new UserStats();
        when(userStatsRepository.findById(userId)).thenReturn(Optional.of(mockedUserStats));

        UserStats result = userStatsService.getUserStatsForUser(userId);

        verify(userStatsRepository).findById(userId);
        Assertions.assertNotNull(result);
    }

    @Test
    void updateUserStatsShouldUpdateStats() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Game game = new Game("Test Game", "http://test.com", GameCategory.MATH);
        Event event = new Event("completed", ZonedDateTime.now(), game);
        event.setUser(user);

        UserStats userStats = new UserStats();
        when(userStatsRepository.findById(userId)).thenReturn(Optional.of(userStats));

        userStatsService.updateUserStats(event);

        verify(userStatsRepository).findById(userId);
        verify(userStatsRepository).save(any(UserStats.class));
    }
}
