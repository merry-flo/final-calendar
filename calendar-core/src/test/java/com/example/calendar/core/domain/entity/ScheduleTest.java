package com.example.calendar.core.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleTest {

    @DisplayName("스케줄 생성 및 도메인으로 변환 테스트")
    @Test
    void test1() {
        // given
        User user = new User("세형", "test@gmail.com", "password", LocalDate.now());
        LocalDateTime taskAt = LocalDateTime.now();
        Schedule schedule = Schedule.task(taskAt, "앱 만들기", "앱 만들어서 수익화 하즈아~!", user);

        // when && then
        Assertions.assertThat(schedule.getScheduleType()).isEqualTo(ScheduleType.TASK);
        Assertions.assertThat(schedule.getStartAt()).isEqualTo(taskAt);
        Assertions.assertThat(schedule.toTask().getTitle()).isEqualTo(schedule.getTitle());
    }
}