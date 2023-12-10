package com.elevate.api.event;

import com.elevate.api.game.Game;
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
    private String occurredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    public Event() {

    }

    public Event(String type, String occurredAt, Game game) {
        this.type = type;
        this.occurredAt = occurredAt;
        this.game = game;
    }
}
