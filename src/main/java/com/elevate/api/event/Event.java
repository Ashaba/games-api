package com.elevate.api.event;

import com.elevate.api.game.Game;
import com.elevate.api.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "events")
@AllArgsConstructor
@Getter @Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String type;

    /**
     * Going to make an assumption that the timestamp string from the request is in a standard format like ISO 8601
     */
    @Column(name = "occurred_at", nullable = false)
    @JsonProperty("occurred_at")
    private String occurredAt;

    @Column(name = "created_at", nullable = false)
    @JsonIgnore
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Event() {

    }

    public Event(String type, String occurredAt, Game game, ZonedDateTime createdAt) {
        this.type = type;
        this.occurredAt = occurredAt;
        this.game = game;
        this.createdAt = createdAt;
    }
}
