package com.example.calendar.api.controller.api;

import com.example.calendar.api.dto.AuthUser;
import com.example.calendar.api.dto.TaskCreateRequest;
import com.example.calendar.api.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/schedules")
@RestController
public class ScheduleApiController {
    private final TaskService taskService;

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(@RequestBody TaskCreateRequest request, AuthUser authUser) {
        taskService.createTask(request, authUser);
        return ResponseEntity.ok().build();
    }


}
