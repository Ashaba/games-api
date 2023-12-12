package com.elevate.api.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Setter
@Getter
public class EventDTO implements Serializable {
    private String type;
    @JsonProperty("occurred_at")
    private ZonedDateTime occurredAt;
    @JsonProperty("game_id")
    private Long gameId;
}
