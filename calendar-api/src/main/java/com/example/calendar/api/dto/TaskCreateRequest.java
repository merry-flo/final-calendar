package com.example.calendar.api.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TaskCreateRequest {
    private final String title;
    private final String description;
    private final LocalDateTime taskAt;
}
