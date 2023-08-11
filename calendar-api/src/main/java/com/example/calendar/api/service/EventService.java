package com.example.calendar.api.service;

import com.example.calendar.api.dto.AuthUser;
import com.example.calendar.api.dto.EngagementMailDto;
import com.example.calendar.api.dto.EventCreateReq;
import com.example.calendar.core.domain.RequestStatus;
import com.example.calendar.core.domain.entity.Engagement;
import com.example.calendar.core.domain.entity.Schedule;
import com.example.calendar.core.domain.entity.User;
import com.example.calendar.core.domain.entity.repository.EngagementRepository;
import com.example.calendar.core.domain.entity.repository.ScheduleRepository;
import com.example.calendar.core.service.UserService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final UserService userService;
    private final EngagementService engagementService;

    private final EngagementRepository engagementRepository;
    private final ScheduleRepository scheduleRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createEvent(EventCreateReq eventCreateReq, AuthUser authUser) {
        engagementService.validateOverlapped(
            eventCreateReq.getAttendeeIds(),
            eventCreateReq.getStartAt(),
            eventCreateReq.getEndAt());

        User from = userService.findUserOrElseThrow(authUser.getUserId());
        final Schedule event = Schedule.event(
            eventCreateReq.getStartAt(),
            eventCreateReq.getEndAt(),
            eventCreateReq.getTitle(),
            eventCreateReq.getDescription(),
            from
        );

        scheduleRepository.save(event);
        List<User> attendees = userService.findAllUserByUserIds(eventCreateReq.getAttendeeIds());

        Set<String> toList = attendees.stream()
                                      .map(User::getEmail)
                                      .filter(email -> !email.equals(from.getEmail()))
                                      .collect(Collectors.toSet());
        attendees.stream()
                 .filter(attendee1 -> !attendee1.equals(from))
                 .forEach(attendee -> {
                     final Engagement engagement = Engagement.builder()
                                                             .schedule(event)
                                                             .attendee(attendee)
                                                             .requestStatus(RequestStatus.REQUESTED)
                                                             .build();

                     engagementRepository.save(engagement);

                     eventPublisher.publishEvent(
                         new EngagementMailDto(engagement.getId(), from.getEmail(), attendee.getEmail(), toList,
                             event.getTitle(), event.getPeriod()));
                 });
    }
}
