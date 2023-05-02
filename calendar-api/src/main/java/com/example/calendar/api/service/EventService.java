package com.example.calendar.api.service;

import com.example.calendar.api.dto.AuthUser;
import com.example.calendar.api.dto.EventCreateReq;
import com.example.calendar.core.domain.RequestStatus;
import com.example.calendar.core.domain.entity.Engagement;
import com.example.calendar.core.domain.entity.Schedule;
import com.example.calendar.core.domain.entity.User;
import com.example.calendar.core.domain.entity.repository.EngagementRepository;
import com.example.calendar.core.domain.entity.repository.ScheduleRepository;
import com.example.calendar.core.service.UserService;
import java.util.List;
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

        if (engagements.stream().anyMatch(e ->
            eventCreateReq.getAttendeeIds().contains(e.getAttendee().getId())
                && e.getRequestStatus() == RequestStatus.ACCEPTED
                && e.getEvent().isOverlapped(eventCreateReq.getStartAt(), eventCreateReq.getEndAt()))) {
            throw new RuntimeException("can not create event because of overlapped event.");
        }

        final Schedule event = Schedule.event(
            eventCreateReq.getStartAt(),
            eventCreateReq.getEndAt(),
            eventCreateReq.getTitle(),
            eventCreateReq.getDescription(),
            userService.findUserOrElseThrow(authUser.getUserId())
        );

        scheduleRepository.save(event);

        eventCreateReq.getAttendeeIds().forEach(attendeeId -> {
            final User user = userService.findUserOrElseThrow(attendeeId);
            final Engagement engagement = Engagement.builder()
                                         .schedule(event)
                                         .attendee(user)
                                         .requestStatus(RequestStatus.REQUESTED)
                                         .build();

            engagementRepository.save(engagement);
            emailService.sendEngagement(engagement);
        });
    }
}
