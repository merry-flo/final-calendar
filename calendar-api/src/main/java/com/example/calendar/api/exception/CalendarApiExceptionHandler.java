package com.example.calendar.api.exception;

import com.example.calendar.core.exception.CalendarErrorResponse;
import com.example.calendar.core.exception.CalendarException;
import com.example.calendar.core.exception.ErrorCode;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CalendarApiExceptionHandler {

    @ExceptionHandler(CalendarException.class)
    public ResponseEntity<CalendarErrorResponse> handleCalendarException(CalendarException e, HttpServletRequest req) {
        log.error("CalendarException occurred : {}", e.getMessage(), e);
        ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(
            new CalendarErrorResponse(errorCode, errorCode.getDefaultMessage(), req.getRequestURI()),
            ErrorCodeHttpStatusMapper.getHttpStatus(errorCode));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CalendarErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest req) {
        log.error("MethodArgumentNotValidException occurred : {}", e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.VALIDATION_FAILURE;

        return new ResponseEntity<>(
            new CalendarErrorResponse(errorCode,
                Optional.ofNullable(e.getBindingResult().getFieldError())
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .orElseGet(errorCode::getDefaultMessage),
                req.getRequestURI()),
            ErrorCodeHttpStatusMapper.getHttpStatus(errorCode));
    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<CalendarErrorResponse> handleException(Exception e, HttpServletRequest req) {
        log.error("Unexpected exception occurred : {}", e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(
            new CalendarErrorResponse(errorCode, errorCode.getDefaultMessage(), req.getRequestURI()),
            ErrorCodeHttpStatusMapper.getHttpStatus(errorCode));
    }

}
