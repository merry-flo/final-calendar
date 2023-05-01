package com.example.calendar.core.domain;

import com.example.calendar.core.domain.entity.Schedule;
import lombok.Getter;

@Getter
public class Event {
    private Schedule schedule;

    public Event(Schedule schedule) {
        this.schedule = schedule;
    }
}
