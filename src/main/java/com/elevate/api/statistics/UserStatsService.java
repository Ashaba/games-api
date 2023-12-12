package com.elevate.api.statistics;

import com.elevate.api.event.Event;
import com.elevate.api.event.EventRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserStatsService {

    private final EventRepository eventRepository;
    private final UserStatsRepository userStatsRepository;

    public UserStatsService(EventRepository eventRepository, UserStatsRepository userStatsRepository) {
        this.eventRepository = eventRepository;
        this.userStatsRepository = userStatsRepository;
    }

    public UserStats getUserStatsForUser(Long userId) {
        UserStats userStats = userStatsRepository.findById(userId).orElse(new UserStats());

        int currentStreak = calculateCurrentStreak(userId);
        userStats.setCurrentStreakInDays(currentStreak);
        return userStats;
    }

    @Async
    public void updateUserStats(Event event) {
        Long userId = event.getUser().getId();
        UserStats userStats = userStatsRepository.findById(userId).orElse(new UserStats());
        userStats.setTotalGamesPlayed(userStats.getTotalGamesPlayed() + 1);
        switch (event.getGame().getCategory()) {
            case MATH:
                userStats.setTotalMathGamesPlayed(userStats.getTotalMathGamesPlayed() + 1);
                break;
            case READING:
                userStats.setTotalReadingGamesPlayed(userStats.getTotalReadingGamesPlayed() + 1);
                break;
            case SPEAKING:
                userStats.setTotalSpeakingGamesPlayed(userStats.getTotalSpeakingGamesPlayed() + 1);
                break;
            case WRITING:
                userStats.setTotalWritingGamesPlayed(userStats.getTotalWritingGamesPlayed() + 1);
                break;
        }
        userStatsRepository.save(userStats);
    }

    private int calculateCurrentStreak(Long userId) {
        List<Event> events = eventRepository.findByUserIdOrderByOccurredAtDesc(userId);
        if (events.isEmpty()) {
            return 0;
        }
        int currentStreak = 0;
        LocalDate lastDate = LocalDate.now();
        for (Event event : events) {
            LocalDate eventDate = event.getOccurredAt().toLocalDate();
            if (eventDate.isEqual(lastDate.minusDays(1))) {
                currentStreak++;
            } else {
                break;
            }
            lastDate = eventDate;
        }
        return currentStreak;
    }
}
