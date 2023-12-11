package com.elevate.api.event;

import com.elevate.api.statistics.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository repository;
    private final UserStatsService userStatsService;

    @Autowired
    public EventService(EventRepository repository, UserStatsService userStatsService) {
        this.repository = repository;
        this.userStatsService = userStatsService;
    }

    public Event createEvent(Event event) {
        Event savedEvent = repository.save(event);

        userStatsService.updateUserStats(savedEvent);
        return savedEvent;
    }
}
