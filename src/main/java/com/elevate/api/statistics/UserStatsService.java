package com.elevate.api.statistics;

import com.elevate.api.event.Event;
import com.elevate.api.event.EventRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
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

    /**
     * Calculates the current streak for a user by fetching all events for the user and checking the dates.
     * If the user has no events, the streak is 0. If the user has events, the streak is calculated by
     * checking the dates of the events and comparing them to the current date. If the event is on the same
     * day or the day before, the streak is incremented. If the event is on a day before that, the streak is
     * broken and the loop is exited.
     * @param userId long
     * @return int
     */
    public int calculateCurrentStreak(Long userId) {
        List<Event> events = eventRepository.findByUserIdOrderByCreatedAtDesc(userId);

        if (events.isEmpty()) {
            return 0;
        }

        int currentStreak = 0;
        LocalDate lastDate = LocalDate.now(ZoneId.of("UTC"));
        LocalDate lastProcessedDate = null;

        for (Event event : events) {
            LocalDate eventDate = event.getCreatedAt().toLocalDate();

            if (eventDate.equals(lastProcessedDate)) {
                continue;
            }

            if (eventDate.isEqual(lastDate) || eventDate.isEqual(lastDate.minusDays(1))) {
                currentStreak++;
                lastDate = eventDate;
            } else {
                break;
            }

            lastProcessedDate = eventDate;
        }

        return currentStreak;
    }
}
