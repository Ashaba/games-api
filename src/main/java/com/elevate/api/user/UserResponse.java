package com.elevate.api.user;

import com.elevate.api.statistics.UserStats;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("stats")
    private UserStats userStats;
}
