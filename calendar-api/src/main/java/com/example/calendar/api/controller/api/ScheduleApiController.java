package com.example.calendar.api.controller.api;

import com.example.calendar.api.dto.AuthUser;
import com.example.calendar.api.dto.EventCreateReq;
import com.example.calendar.api.dto.NotificationCreateReq;
import com.example.calendar.api.dto.ReplyEngagementReq;
import com.example.calendar.api.dto.ScheduleDto;
import com.example.calendar.api.dto.TaskCreateRequest;
import com.example.calendar.api.service.EngagementService;
import com.example.calendar.api.service.EventService;
import com.example.calendar.api.service.NotificationService;
import com.example.calendar.api.service.ScheduleQueryService;
import com.example.calendar.api.service.TaskService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/schedules")
@RestController
public class ScheduleApiController {
    private final ScheduleQueryService scheduleQueryService;
    private final TaskService taskService;
    private final EventService eventService;
    private final NotificationService notificationService;
    private final EngagementService engagementService;

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(@RequestBody @Valid TaskCreateRequest request, AuthUser authUser) {
        taskService.createTask(request, authUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/events")
    public ResponseEntity<Void> createEvents(@RequestBody @Valid EventCreateReq request, AuthUser authUser) {
        eventService.createEvent(request, authUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications")
    public ResponseEntity<Void> createNotification(@RequestBody @Valid NotificationCreateReq request, AuthUser authUser) {
        notificationService.createNotification(request, authUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/day")
    public ResponseEntity<List<ScheduleDto>> getDaySchedule(
        AuthUser authUser,
        @DateTimeFormat(iso = ISO.DATE)
        @RequestParam(required = false) LocalDate date
    ) {
        List<ScheduleDto> schedules = scheduleQueryService.getDaySchedule(
            authUser,
            date == null ? LocalDate.now() : date);

        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/week")
    public ResponseEntity<List<ScheduleDto>> getWeekSchedule(
        AuthUser authUser,
        @DateTimeFormat(iso = ISO.DATE)
        @RequestParam(required = false) LocalDate date
    ) {
        List<ScheduleDto> schedules = scheduleQueryService.getWeekSchedule(
            authUser,
            date == null ? LocalDate.now() : date);

        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/month")
    public ResponseEntity<List<ScheduleDto>> getMonthSchedule(
        AuthUser authUser,
        @DateTimeFormat(pattern = "yyyy-MM")
        @RequestParam(required = false) YearMonth date
    ) {
        List<ScheduleDto> schedules = scheduleQueryService.getMonthSchedule(
            authUser,
            date == null ? YearMonth.now() : date);

        return ResponseEntity.ok(schedules);
    }

    @PutMapping("/events/engagements/{engagementId}")
    public ResponseEntity<Void> updateEngagement(
        @RequestBody @Valid ReplyEngagementReq req,
        @PathVariable Long engagementId, AuthUser authUser) {
        engagementService.update(req.getType(), engagementId, authUser);
        return ResponseEntity.ok().build();
    }

}
