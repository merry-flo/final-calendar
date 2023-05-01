package com.example.calendar.core.domain.entity;

import com.example.calendar.core.domain.Event;
import com.example.calendar.core.domain.Notification;
import com.example.calendar.core.domain.Task;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "schedules")
@Entity
public class Schedule extends BaseEntity{

    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String title;
    private String description;

    @JoinColumn(name = "writer_id")
    @ManyToOne
    private User writer;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @Builder(access = AccessLevel.PRIVATE)
    public Schedule(LocalDateTime startAt, LocalDateTime endAt, String title, String description, User writer,
        ScheduleType scheduleType) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.title = title;
        this.description = description;
        this.writer = writer;
        this.scheduleType = scheduleType;
    }

    public static Schedule event(LocalDateTime startAt, LocalDateTime endAt, String title, String description,
        User writer) {
        return Schedule.builder()
                       .startAt(startAt)
                       .endAt(endAt)
                       .title(title)
                       .description(description)
                       .writer(writer)
                       .scheduleType(ScheduleType.EVENT)
                       .build();
    }

    public static Schedule task(LocalDateTime taskAt, String title, String description, User writer) {
        return Schedule.builder()
                       .startAt(taskAt)
                       .title(title)
                       .description(description)
                       .writer(writer)
                       .scheduleType(ScheduleType.TASK)
                       .build();
    }

    public static Schedule notification(LocalDateTime notifyAt, String title, User writer) {
        return Schedule.builder()
                       .startAt(notifyAt)
                       .title(title)
                       .writer(writer)
                       .scheduleType(ScheduleType.NOTIFICATION)
                       .build();
    }

    public Event toEvent() {
        return new Event(this);
    }

    public Task toTask() {
        return new Task(this);
    }

    public Notification toNotification() {
        return new Notification(this);
    }
}
