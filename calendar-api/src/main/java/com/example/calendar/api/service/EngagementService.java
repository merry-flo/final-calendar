package com.example.calendar.api.service;

import com.example.calendar.api.dto.AuthUser;
import com.example.calendar.core.domain.RequestReplyType;
import com.example.calendar.core.domain.RequestStatus;
import com.example.calendar.core.domain.entity.repository.EngagementRepository;
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

}
