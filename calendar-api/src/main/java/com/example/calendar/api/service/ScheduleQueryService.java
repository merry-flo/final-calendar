package com.example.calendar.api.service;

import com.example.calendar.api.dto.AuthUser;
import com.example.calendar.api.dto.ScheduleDto;
import com.example.calendar.api.utils.DtoConverter;
import com.example.calendar.core.domain.entity.repository.EngagementRepository;
import com.example.calendar.core.domain.entity.repository.ScheduleRepository;
import com.example.calendar.core.util.Period;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;
    private final EngagementRepository engagementRepository;

    public List<ScheduleDto> getDaySchedule(AuthUser authUser, LocalDate date) {
        return getScheduleList(authUser, Period.of(date, date));
    }

    public List<ScheduleDto> getWeekSchedule(AuthUser authUser, LocalDate date) {
        return getScheduleList(authUser, Period.of(date, date.plusDays(6)));
    }

    public List<ScheduleDto> getMonthSchedule(AuthUser authUser, YearMonth date) {
        return getScheduleList(authUser, Period.of(date.atDay(1), date.atEndOfMonth()));
    }

    private List<ScheduleDto> getScheduleList(AuthUser authUser, Period period) {
        return Stream.concat(
                         scheduleRepository.findAllByWriter_Id(authUser.getUserId())
                                           .stream()
                                           .filter(schedule -> schedule.isOverlapped(period))
                                           .map(DtoConverter::fromSchedule),
                         engagementRepository.findAllByAttendee_Id(authUser.getUserId())
                                             .stream()
                                             .filter(engagement -> engagement.isOverlapped(period))
                                             .map(engagement -> DtoConverter.fromSchedule(engagement.getSchedule())))
                     .collect(Collectors.toUnmodifiableList());
    }
}
