package com.example.calendar.api.exception;


import com.example.calendar.core.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class ErrorCodeHttpStatusMapper {
    public static HttpStatus getHttpStatus(ErrorCode errorCode) {
        switch (errorCode) {
            case LOGIN_FAILURE:
            case NO_SESSION:
                return HttpStatus.UNAUTHORIZED;

            case USER_ALREADY_EXIST:
            case USER_NOT_FOUND:
            case SCHEDULE_OVERLAPPED:
            case NOT_SUPPORTED_OPERATION:
            case VALIDATION_FAILURE:
            case BAD_REQUEST:
                return HttpStatus.BAD_REQUEST;

            case INTERNAL_SERVER_ERROR:
                return HttpStatus.INTERNAL_SERVER_ERROR;

            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
