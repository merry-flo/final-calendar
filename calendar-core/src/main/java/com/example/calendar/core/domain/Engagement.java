package com.example.calendar.core.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Engagement {
    private Long id;
    private Event event;
    private User attendee;
    private LocalDateTime createdAt;
    private RequestStatus requestStatus;
}
