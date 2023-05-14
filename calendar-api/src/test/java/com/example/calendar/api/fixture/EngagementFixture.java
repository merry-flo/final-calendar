package com.example.calendar.api.fixture;

import com.example.calendar.core.domain.RequestStatus;
import com.example.calendar.core.domain.entity.Engagement;
import com.example.calendar.core.domain.entity.Schedule;
import java.time.LocalDate;

public abstract class EngagementFixture {
    public static Engagement fixture() {
        return Engagement.builder()
                         .schedule(ScheduleFixture.eventFixture())
                         .attendee(
                             UserFixture.fixture("test1@gmail.com", "password", "test1", LocalDate.of(1993, 1, 1)))
                         .requestStatus(RequestStatus.ACCEPTED)
                         .build();
    }
}
