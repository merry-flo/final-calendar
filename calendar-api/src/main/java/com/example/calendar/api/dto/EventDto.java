package com.example.calendar.api.dto;

import com.example.calendar.core.domain.entity.ScheduleType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventDto implements ScheduleDto{

    private Long scheduleId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String title;
    private String description;
    private Long writerId;

    public EventDto(Long scheduleId, LocalDateTime startAt, LocalDateTime endAt, String title, String description,
        Long writerId) {
        this.scheduleId = scheduleId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.title = title;
        this.description = description;
        this.writerId = writerId;
    }

    @Override
    public ScheduleType getScheduleType() {
        return ScheduleType.EVENT;
    }
}
