package com.example.calendar.api.utils;

import com.example.calendar.api.dto.EventDto;
import com.example.calendar.api.dto.NotificationDto;
import com.example.calendar.api.dto.ScheduleDto;
import com.example.calendar.api.dto.TaskDto;
import com.example.calendar.core.domain.entity.Schedule;
import com.example.calendar.core.exception.CalendarException;
import com.example.calendar.core.exception.ErrorCode;

public abstract class DtoConverter {

    public static ScheduleDto fromSchedule(Schedule schedule) {
        switch (schedule.getScheduleType()) {
            case TASK:
                return new TaskDto(
                    schedule.getId(),
                    schedule.getStartAt(),
                    schedule.getTitle(),
                    schedule.getDescription(),
                    schedule.getWriter().getId());
            case EVENT:
                return new EventDto(
                    schedule.getId(),
                    schedule.getStartAt(),
                    schedule.getEndAt(),
                    schedule.getTitle(),
                    schedule.getDescription(),
                    schedule.getWriter().getId()
                );
            case NOTIFICATION:
                return new NotificationDto(
                    schedule.getId(),
                    schedule.getStartAt(),
                    schedule.getTitle(),
                    schedule.getWriter().getId()
                );
            default:
                throw new CalendarException(ErrorCode.NOT_SUPPORTED_OPERATION);
        }
    }
}
