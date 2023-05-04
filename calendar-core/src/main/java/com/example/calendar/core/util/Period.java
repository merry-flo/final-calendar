package com.example.calendar.core.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Period {
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    private Period(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt == null ? startAt : endAt;
    }

    public static Period of (LocalDateTime startAt, LocalDateTime endAt) {
        return new Period(startAt, endAt);
    }

    public static Period of (LocalDate startAt, LocalDate endAt) {
        return new Period(startAt.atStartOfDay(), endAt.atTime(23, 59, 59, 999999999));
    }

    public boolean isOverlapped(LocalDateTime startAt, LocalDateTime endAt) {
        return this.startAt.isBefore(endAt) && this.endAt.isAfter(startAt);
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }
}
