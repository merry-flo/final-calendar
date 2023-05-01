package com.example.calendar.core.domain;

import com.example.calendar.core.domain.entity.Schedule;
import lombok.Getter;

@Getter
public class Task {
    private Schedule schedule;

    public Task(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getTitle() {
        return schedule.getTitle();
    }
}
