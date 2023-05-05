package com.example.calendar.api.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskCreateRequest {
    @NotBlank
    private final String title;

    @NotBlank
    private final String description;

    @NotNull
    private final LocalDateTime taskAt;
}
