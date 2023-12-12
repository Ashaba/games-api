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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        Event event = new Event("completed", "12:15", game, ZonedDateTime.now());
        event.setUser(user);

        UserStats userStats = new UserStats();
        when(userStatsRepository.findById(userId)).thenReturn(Optional.of(userStats));

        userStatsService.updateUserStats(event);

        verify(userStatsRepository).findById(userId);
        verify(userStatsRepository).save(any(UserStats.class));
    }

    @Test
    void calculateCurrentStreakWithNoEventsShouldReturnZero() {
        Long userId = 1L;
        when(eventRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(Collections.emptyList());

        int streak = userStatsService.calculateCurrentStreak(userId);

        Assertions.assertEquals(0, streak);
    }

    @Test
    void calculateCurrentStreakWithConsecutiveDaysShouldReturnCorrectStreak() {
        Long userId = 1L;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        List<Event> events = Arrays.asList(
                new Event("type", "occurredAt", null, now),
                new Event("type", "occurredAt", null, now.minusDays(1)),
                new Event("type", "occurredAt", null, now.minusDays(2))
        );
        when(eventRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(events);

        int streak = userStatsService.calculateCurrentStreak(userId);

        Assertions.assertEquals(3, streak);
    }

    @Test
    void calculateCurrentStreakWithNonConsecutiveDaysShouldReturnShorterStreak() {
        Long userId = 1L;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        List<Event> events = Arrays.asList(
                new Event("type", "occurredAt", null, now),
                new Event("type", "occurredAt", null, now.minusDays(2)),
                new Event("type", "occurredAt", null, now.minusDays(3))
        );
        when(eventRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(events);

        int streak = userStatsService.calculateCurrentStreak(userId);

        Assertions.assertEquals(1, streak);
    }

    @Test
    void calculateCurrentStreakWithEventsHavingGapsShouldReturnCorrectStreak() {
        Long userId = 1L;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        List<Event> events = Arrays.asList(
                new Event("type", "occurredAt", null, now),
                new Event("type", "occurredAt", null, now.minusDays(1)),
                new Event("type", "occurredAt", null, now.minusDays(3))
        );
        when(eventRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(events);

        int streak = userStatsService.calculateCurrentStreak(userId);

        Assertions.assertEquals(2, streak);
    }
}
