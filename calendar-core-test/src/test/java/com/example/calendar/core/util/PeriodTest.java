package com.example.calendar.core.util;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PeriodTest {

    @ParameterizedTest
    @MethodSource("testPeriodDataSet")
    void testForPeriod(LocalDateTime startAt, LocalDateTime endAt, boolean result) {
        YearMonth yearMonth = YearMonth.parse("2023-04");
        Period period = Period.of(yearMonth.atDay(1), yearMonth.atEndOfMonth());
        Assertions.assertThat(period.isOverlapped(startAt, endAt)).isEqualTo(result);
    }


    private static Stream<Arguments> testPeriodDataSet() {
        return Stream.of(
            Arguments.of(
                LocalDateTime.of(2023, 4, 3, 0, 0),
                LocalDateTime.of(2023, 4, 13, 2, 0),
                true
            ),
            Arguments.of(
                LocalDateTime.of(2023, 3, 21, 0, 0),
                LocalDateTime.of(2023, 4, 1, 2, 0),
                true
            ),
            Arguments.of(
                LocalDateTime.of(2023, 3, 30, 0, 0),
                LocalDateTime.of(2023, 3, 31, 0, 0),
                false
            ),
            Arguments.of(
                LocalDateTime.of(2023, 4, 30, 0, 0),
                LocalDateTime.of(2023, 4, 30, 1, 0),
                true
            ),
            Arguments.of(
                LocalDateTime.of(2023, 4, 30, 23, 59, 59, 8912),
                LocalDateTime.of(2023, 5, 1, 0, 0),
                true
            ),
            Arguments.of(
                LocalDateTime.of(2023, 5, 1, 0, 0),
                LocalDateTime.of(2023, 5, 1, 0, 0, 0, 1),
                false
            )
        );
    }
}