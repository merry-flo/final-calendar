package com.example.calendar.api.dto;

import com.example.calendar.core.domain.entity.ScheduleType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TaskDto implements ScheduleDto{

    private Long scheduleId;
    private LocalDateTime taskAt;
    private String title;
    private String description;
    private Long writerId;

    public TaskDto(Long scheduleId, LocalDateTime taskAt, String title, String description, Long writerId) {
        this.scheduleId = scheduleId;
        this.taskAt = taskAt;
        this.title = title;
        this.description = description;
        this.writerId = writerId;
    }

    @Override
    public ScheduleType getScheduleType() {
        return ScheduleType.TASK;
    }

}
