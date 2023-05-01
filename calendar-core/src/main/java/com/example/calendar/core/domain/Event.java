package com.example.calendar.core.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Event {
    private Long id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String title;
    private String description;
    private User writer;
    private LocalDateTime createdAt;

    private List<Engagement> engagements;
}
