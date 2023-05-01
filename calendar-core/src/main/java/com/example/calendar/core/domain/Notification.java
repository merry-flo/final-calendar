package com.example.calendar.core.domain;


import com.example.calendar.core.domain.entity.Schedule;
import lombok.Getter;

@Getter
public class Notification {
    private Schedule schedule;

    public Notification(Schedule schedule) {
        this.schedule = schedule;
    }
}
