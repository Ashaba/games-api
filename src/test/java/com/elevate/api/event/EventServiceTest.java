package com.elevate.api.event;

import com.elevate.api.game.Game;
import com.elevate.api.statistics.UserStatsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository repository;
    @Mock
    private UserStatsService userStatsService;

    @InjectMocks
    private EventService service;

    @Test
    void createEventSuccess() {
        Event event = new Event("occurred", "12:00pm", new Game());
        when(repository.save(Mockito.any(Event.class))).thenReturn(event);

        Event createdEvent = service.createEvent(event);

        Assertions.assertNotNull(createdEvent);
        Assertions.assertEquals(createdEvent.getGame(), event.getGame());
        Assertions.assertEquals(createdEvent.getType(), event.getType());
        Assertions.assertEquals(createdEvent.getOccurredAt(), event.getOccurredAt());
        verify(repository).save(event);
        verify(userStatsService).updateUserStats(event);
    }

    @Test
    void createEventSuccessShouldCreateUserStats() {
        Event event = new Event("occurred", "12:00pm", new Game());
        when(repository.save(Mockito.any(Event.class))).thenReturn(event);

        Event createdEvent = service.createEvent(event);

        Assertions.assertNotNull(createdEvent);
        Assertions.assertEquals(createdEvent.getGame(), event.getGame());
        Assertions.assertEquals(createdEvent.getType(), event.getType());
        Assertions.assertEquals(createdEvent.getOccurredAt(), event.getOccurredAt());
        verify(repository).save(event);
        verify(userStatsService).updateUserStats(event);
    }

    @Test
    void createEventFailure() {
        Event event = new Event("occurred", "12:00pm", new Game());
        when(repository.save(Mockito.any(Event.class))).thenReturn(null);

        Event createdEvent = service.createEvent(event);

        Assertions.assertNull(createdEvent);
        verify(repository).save(event);
        verify(userStatsService, Mockito.never()).updateUserStats(event);
    }
}