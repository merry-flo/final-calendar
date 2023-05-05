package com.example.calendar.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CalendarErrorResponse {
    private final ErrorCode errorCode;
    private final String message;
    private final String apiPath;
}
