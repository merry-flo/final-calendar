package com.example.calendar.api.service;

import com.example.calendar.api.dto.AuthUser;
import com.example.calendar.api.dto.EngagementMailDto;
import com.example.calendar.api.dto.EventCreateReq;
import com.example.calendar.core.domain.RequestStatus;
import com.example.calendar.core.domain.entity.BaseEntity;
import com.example.calendar.core.domain.entity.Engagement;
import com.example.calendar.core.domain.entity.Schedule;
import com.example.calendar.core.domain.entity.User;
import com.example.calendar.core.domain.entity.repository.EngagementRepository;
import com.example.calendar.core.domain.entity.repository.ScheduleRepository;
import com.example.calendar.core.exception.CalendarException;
import com.example.calendar.core.exception.ErrorCode;
import com.example.calendar.core.service.UserService;
import com.example.calendar.core.util.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EmailService emailService;
    private final UserService userService;
    private final EngagementRepository engagementRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void createEvent(EventCreateReq eventCreateReq, AuthUser authUser) {
        final List<Engagement> engagements = engagementRepository.findAll();

        Period period = Period.of(eventCreateReq.getStartAt(), eventCreateReq.getEndAt());
        if (engagements.stream().anyMatch(e -> eventCreateReq.getAttendeeIds().contains(e.getAttendee().getId())
            && e.getRequestStatus() == RequestStatus.ACCEPTED
            && e.getSchedule().isOverlapped(period))) {
            throw new CalendarException(ErrorCode.SCHEDULE_OVERLAPPED);
        }

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

        Set<String> toList = attendees.stream().map(User::getEmail).collect(Collectors.toSet());

        attendees.stream().forEach(attendee -> {
            final Engagement engagement = Engagement.builder()
                                         .schedule(event)
                                         .attendee(attendee)
                                         .requestStatus(RequestStatus.REQUESTED)
                                         .build();

            engagementRepository.save(engagement);

            emailService.sendEngagement(
                new EngagementMailDto(engagement.getId(), from.getEmail(), attendee.getEmail(), toList,
                    event.getTitle(), event.getPeriod()));
        });
    }
}
