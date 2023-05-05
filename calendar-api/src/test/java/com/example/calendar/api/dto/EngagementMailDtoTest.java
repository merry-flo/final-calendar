package com.example.calendar.api.dto;

import com.example.calendar.core.util.Period;
import java.time.LocalDateTime;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EngagementMailDtoTest {

    @Test
    void test() {

        EngagementMailDto mailDto = new EngagementMailDto(
            1L, "from@gmail.com", "to@gmail.com", Collections.emptySet(),
            "title", Period.of(
            LocalDateTime.of(2021, 8, 1, 10, 0),
            LocalDateTime.of(2021, 8, 1, 18, 0))
        );

        Assertions.assertThat(mailDto.getEndAtFormat()).isEqualTo("a hh시 mm분");

        mailDto = new EngagementMailDto(
            1L, "from@gmail.com", "to@gmail.com", Collections.emptySet(),
            "title", Period.of(
            LocalDateTime.of(2021, 8, 1, 10, 0),
            LocalDateTime.of(2021, 8, 3, 18, 0))
        );

        Assertions.assertThat(mailDto.getEndAtFormat()).isEqualTo("dd일(E) a hh시 mm분");

        mailDto = new EngagementMailDto(
            1L, "from@gmail.com", "to@gmail.com", Collections.emptySet(),
            "title", Period.of(
            LocalDateTime.of(2021, 8, 1, 10, 0),
            LocalDateTime.of(2021, 9, 3, 18, 0))
        );

        Assertions.assertThat(mailDto.getEndAtFormat()).isEqualTo("MM월 dd일(E) a hh시 mm분");

    }
}