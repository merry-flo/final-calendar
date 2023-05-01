package com.example.calendar.api.service;

import com.example.calendar.api.dto.AuthUser;
import com.example.calendar.api.dto.TaskCreateRequest;
import com.example.calendar.core.domain.entity.Schedule;
import com.example.calendar.core.domain.entity.repository.ScheduleRepository;
import com.example.calendar.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final UserService userService;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void createTask(TaskCreateRequest request, AuthUser authUser) {
        var user = userService.findUserOrElseThrow(authUser.getUserId());
        var task = Schedule.task(
            request.getTaskAt(),
            request.getTitle(),
            request.getDescription(),
            user
        );

        scheduleRepository.save(task);
    }
}
