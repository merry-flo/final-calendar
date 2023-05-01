package com.example.calendar.core.domain;


import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification {

    private Long id;
    private LocalDateTime notifyAt;
    private String title;
    private User writer;
    private LocalDateTime createdAt;
}
