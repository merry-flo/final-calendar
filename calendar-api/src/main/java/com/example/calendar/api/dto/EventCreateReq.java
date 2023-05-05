package com.example.calendar.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventCreateReq {
    @NotBlank
    private final String title;
    @NotBlank
    private final String description;
    @NotNull
    private final LocalDateTime startAt;
    @NotNull
    private final LocalDateTime endAt;

    private final List<Long> attendeeIds;
}
