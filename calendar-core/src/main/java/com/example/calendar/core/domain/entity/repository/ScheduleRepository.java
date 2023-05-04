package com.example.calendar.core.domain.entity.repository;

import com.example.calendar.core.domain.entity.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByWriter_Id(Long userId);

}
