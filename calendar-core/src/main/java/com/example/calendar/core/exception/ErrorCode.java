package com.example.calendar.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    VALIDATION_FAILURE("Validation failure."),
    NO_SESSION("No session!"),

    NOT_SUPPORTED_OPERATION("Not supported operation."),

    USER_ALREADY_EXIST("User already exist."),
    USER_NOT_FOUND("User not found."),

    SCHEDULE_OVERLAPPED("Overlapped schedule."),

    LOGIN_FAILURE("Login failure. Check email and password."),

    INTERNAL_SERVER_ERROR("Internal server error."),

    ;

    private final String defaultMessage;
}
