package com.example.calendar.api.dto;

import lombok.Getter;

@Getter
public class AuthUser {
    private final Long userId;

    private AuthUser(Long userId) {
        this.userId = userId;
    }

    public static AuthUser of(Long userId) {
        return new AuthUser(userId);
    }
}
