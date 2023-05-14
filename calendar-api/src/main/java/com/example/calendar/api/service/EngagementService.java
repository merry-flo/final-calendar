package com.example.calendar.api.service;

import com.example.calendar.api.dto.AuthUser;
import com.example.calendar.core.domain.RequestReplyType;
import com.example.calendar.core.domain.RequestStatus;
import com.example.calendar.core.domain.entity.Engagement;
import com.example.calendar.core.domain.entity.repository.EngagementRepository;
import com.example.calendar.core.exception.CalendarException;
import com.example.calendar.core.exception.ErrorCode;
import com.example.calendar.core.util.Period;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EngagementService {
    private final EngagementRepository engagementRepository;

    @Transactional
    public void update(RequestReplyType type, Long engagementId, AuthUser authUser) {
        engagementRepository.findById(engagementId)
                            .filter(e -> e.getRequestStatus() == RequestStatus.REQUESTED)
                            .filter(e -> e.getAttendee().getId().equals(authUser.getUserId()))
                            .ifPresent(e -> e.updateRequestStatus(type));
    }

    @Transactional(readOnly = true)
    public void validateOverlapped(Set<Long> attendeeIds, LocalDateTime startAt, LocalDateTime endAt) {
        final List<Engagement> engagements
            = engagementRepository.findAllByAttendee_IdInAndRequestStatus(attendeeIds, RequestStatus.ACCEPTED);

        Period period = Period.of(startAt, endAt);
        if (engagements.stream().anyMatch(e -> e.getSchedule().isOverlapped(period))) {
            throw new CalendarException(ErrorCode.SCHEDULE_OVERLAPPED);
        }
    }

}
