package com.elevate.api.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class EventDTO implements Serializable {
    private String type;
    @JsonProperty("occurred_at")
    private String occurredAt;
    @JsonProperty("game_id")
    private Long gameId;
}
