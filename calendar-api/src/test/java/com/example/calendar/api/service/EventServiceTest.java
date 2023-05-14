package com.example.calendar.api.service;

import static org.mockito.ArgumentMatchers.any;

import com.example.calendar.api.dto.AuthUser;
import com.example.calendar.api.dto.EventCreateReq;
import com.example.calendar.api.fixture.UserFixture;
import com.example.calendar.core.domain.entity.User;
import com.example.calendar.core.domain.entity.repository.EngagementRepository;
import com.example.calendar.core.domain.entity.repository.ScheduleRepository;
import com.example.calendar.core.service.UserService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private UserService userService;
    @Mock
    private EngagementService engagementService;
    @Mock
    private EngagementRepository engagementRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @DisplayName("성공 - 이벤트를 생성한다.")
    @Test
    void createEvent() {
        // given
        BDDMockito.willDoNothing().given(engagementService).validateOverlapped(any(), any(), any());
        BDDMockito.given(userService.findUserOrElseThrow(any())).willReturn(UserFixture.fixture());
        BDDMockito.given(scheduleRepository.save(any())).willReturn(null);
        List<User> attendees = UserFixture.fixtures();
        BDDMockito.given(userService.findAllUserByUserIds(any())).willReturn(attendees);
        BDDMockito.given(engagementRepository.save(any())).willReturn(null);

        EventCreateReq eventCreateReq = new EventCreateReq("title", "description",
            LocalDateTime.now().minusDays(2), LocalDateTime.now(), Collections.emptySet());

        // when
        eventService.createEvent(eventCreateReq, AuthUser.of(1L));

        // then
        BDDMockito.verify(scheduleRepository, Mockito.times(1)).save(any());
        BDDMockito.verify(engagementRepository, Mockito.times(3)).save(any());
    }
}