package com.example.calendar.core.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Task {
    private Long id;
    private LocalDateTime taskAt;
    private String title;
    private String description;
    private User writer;
    private LocalDateTime createdAt;
}
