package com.elevate.api.auth;

import lombok.Getter;

@Getter
public class AuthTokenResponse {
    @Getter
    private String token;

    public AuthTokenResponse(String token) {
        this.token = token;
    }
}
