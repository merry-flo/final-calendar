package com.example.calendar.api.service;

import com.example.calendar.api.dto.AuthUser;
import com.example.calendar.api.dto.NotificationCreateReq;
import com.example.calendar.core.domain.entity.Schedule;
import com.example.calendar.core.domain.entity.User;
import com.example.calendar.core.domain.entity.repository.ScheduleRepository;
import com.example.calendar.core.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final UserService userService;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void createNotification(NotificationCreateReq request, AuthUser authUser) {
        User user = userService.findUserOrElseThrow(authUser.getUserId());
        List<LocalDateTime> notifyAtList = request.getRepeatTimes();

        notifyAtList.forEach(notifyAt -> {
            final Schedule notification = Schedule.notification(
                request.getNotifyAt(),
                request.getTitle(),
                user
            );

            scheduleRepository.save(notification);
        });
    }
}
