package com.example.calendar.core.domain.entity.repository;

import com.example.calendar.core.domain.RequestStatus;
import com.example.calendar.core.domain.entity.Engagement;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EngagementRepository extends JpaRepository<Engagement, Long> {

    List<Engagement> findAllByAttendee_Id(Long userId);

    List<Engagement> findAllByAttendee_IdInAndRequestStatus(Set<Long> attendeeIds, RequestStatus accepted);
}
