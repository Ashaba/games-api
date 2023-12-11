package com.elevate.api.event;

import com.elevate.api.game.Game;
import com.elevate.api.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "occurred_at", nullable = false)
    @JsonProperty("occurred_at")
    private String occurredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Event() {

    }

    public Event(String type, String occurredAt, Game game) {
        this.type = type;
        this.occurredAt = occurredAt;
        this.game = game;
    }
}
