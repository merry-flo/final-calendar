package com.example.calendar.core.domain.entity.repository;

import com.example.calendar.core.domain.entity.Engagement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EngagementRepository extends JpaRepository<Engagement, Long> {

    List<Engagement> findAllByAttendee_Id(Long userId);
}
