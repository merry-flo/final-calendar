package com.example.calendar.api.service;

import com.example.calendar.api.dto.EngagementMailDto;
import com.example.calendar.core.domain.entity.Engagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Profile("test")
@Service
public class FakeEmailService implements EmailService{

    @Override
    public void sendEngagement(EngagementMailDto engagementMailDto) {
        log.info("Send email to {}", engagementMailDto.toString());
    }
}
