package com.example.calendar.core.domain;

import com.example.calendar.core.domain.entity.Schedule;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Event {
    private Schedule schedule;

    public Event(Schedule schedule) {
        this.schedule = schedule;
    }

    public boolean isOverlapped(LocalDateTime startAt, LocalDateTime endAt) {
        return schedule.getStartAt().isBefore(endAt) && startAt.isBefore(schedule.getEndAt());
    }
}
