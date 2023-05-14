package com.example.calendar.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import com.example.calendar.api.fixture.EngagementFixture;
import com.example.calendar.core.domain.entity.repository.EngagementRepository;
import com.example.calendar.core.exception.CalendarException;
import java.time.LocalDateTime;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EngagementServiceTest {

    @InjectMocks
    private EngagementService engagementService;

    @Mock
    private EngagementRepository engagementRepository;

    @DisplayName("성공 - engagement 등록 시 overlap 체크")
    @Test
    void given_when_then() {
        // given
        BDDMockito.given(engagementRepository.findAllByAttendee_IdInAndRequestStatus(any(), any()))
                  .willReturn(Collections.singletonList(EngagementFixture.fixture()));

        // when && then
        Assertions.assertThatNoException()
                  .isThrownBy(() -> engagementService.validateOverlapped(Collections.emptySet(),
                      LocalDateTime.of(2023, 2, 1, 1, 0),
                      LocalDateTime.of(2023, 2, 2, 0, 0)));
    }

    @DisplayName("실패 - engagement 등록 시 overlap 체크 걸림")
    @Test
    void fail_whenOverlappedPeriod() {
        // given
        BDDMockito.given(engagementRepository.findAllByAttendee_IdInAndRequestStatus(any(), any()))
                  .willReturn(Collections.singletonList(EngagementFixture.fixture()));

        // when && then
        Assertions.assertThatThrownBy(() -> engagementService.validateOverlapped(Collections.emptySet(),
                      LocalDateTime.of(2023, 1, 31, 1, 0),
                      LocalDateTime.of(2023, 2, 2, 0, 0)))
                  .isInstanceOf(CalendarException.class);
    }
}