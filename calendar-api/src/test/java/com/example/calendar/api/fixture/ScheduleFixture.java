package com.example.calendar.api.fixture;

import com.example.calendar.core.domain.entity.Schedule;
import java.time.LocalDateTime;

public abstract class ScheduleFixture {

    public static Schedule eventFixture() {
        return Schedule.event(
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 2, 1, 0, 0),
            "title",
            "description",
            UserFixture.fixture()
        );
    }
}
