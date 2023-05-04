package com.example.calendar.api.dto;

import com.example.calendar.core.domain.entity.ScheduleType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class NotificationDto implements ScheduleDto {
    private Long scheduleId;
    private LocalDateTime notifyAt;
    private String title;
    private Long writerId;

    public NotificationDto(Long scheduleId, LocalDateTime notifyAt, String title, Long writerId) {
        this.scheduleId = scheduleId;
        this.notifyAt = notifyAt;
        this.title = title;
        this.writerId = writerId;
    }

    @Override
    public ScheduleType getScheduleType() {
        return ScheduleType.NOTIFICATION;
    }
}
