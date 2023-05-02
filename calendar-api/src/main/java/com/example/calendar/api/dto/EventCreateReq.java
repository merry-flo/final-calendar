package com.example.calendar.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class EventCreateReq {
    private final String title;
    private final String description;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final List<Long> attendeeIds;
}
