package com.elevate.api.statistics;

import com.elevate.api.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserStatsTest {
    private UserStats userStats;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User( "testemail@email", "test@username", "test", "test");
        userStats = new UserStats(user, 12,
                1, 6, 0,
                0, 0, null);
    }

    @Test
    void getUser() {
        Assertions.assertEquals(user, userStats.getUser());
    }

    @Test
    void getTotalGamesPlayed() {
        Assertions.assertEquals(12, userStats.getTotalGamesPlayed());
    }

    @Test
    void getTotalMathGamesPlayed() {
        Assertions.assertEquals(1, userStats.getTotalMathGamesPlayed());
    }

    @Test
    void getTotalReadingGamesPlayed() {
        Assertions.assertEquals(6, userStats.getTotalReadingGamesPlayed());
    }

    @Test
    void getTotalSpeakingGamesPlayed() {
        Assertions.assertEquals(0, userStats.getTotalSpeakingGamesPlayed());
    }

    @Test
    void getTotalWritingGamesPlayed() {
        Assertions.assertEquals(0, userStats.getTotalWritingGamesPlayed());
    }

    @Test
    void getCurrentStreakInDays() {
        Assertions.assertEquals(0, userStats.getCurrentStreakInDays());
    }

    @Test
    void getLastPlayedDate() {
        assertNull(userStats.getLastPlayedDate());
    }

    @Test
    void setUser() {
        User user2 = new User();
        userStats.setUser(user2);
        Assertions.assertEquals(user2, userStats.getUser());
    }

    @Test
    void setTotalGamesPlayed() {
        userStats.setTotalGamesPlayed(13);
        Assertions.assertEquals(13, userStats.getTotalGamesPlayed());
    }

    @Test
    void setTotalMathGamesPlayed() {
        userStats.setTotalMathGamesPlayed(2);
        Assertions.assertEquals(2, userStats.getTotalMathGamesPlayed());
    }

    @Test
    void setTotalReadingGamesPlayed() {
        userStats.setTotalReadingGamesPlayed(7);
        Assertions.assertEquals(7, userStats.getTotalReadingGamesPlayed());
    }

    @Test
    void setTotalSpeakingGamesPlayed() {
        userStats.setTotalSpeakingGamesPlayed(1);
        Assertions.assertEquals(1, userStats.getTotalSpeakingGamesPlayed());
    }

    @Test
    void setTotalWritingGamesPlayed() {
        userStats.setTotalWritingGamesPlayed(1);
        Assertions.assertEquals(1, userStats.getTotalWritingGamesPlayed());
    }

    @Test
    void setCurrentStreakInDays() {
        userStats.setCurrentStreakInDays(1);
        Assertions.assertEquals(1, userStats.getCurrentStreakInDays());
    }

    @Test
    void setLastPlayedDate() {
        userStats.setLastPlayedDate(null);
        assertNull(userStats.getLastPlayedDate());
    }
}