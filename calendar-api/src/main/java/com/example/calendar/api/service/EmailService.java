package com.example.calendar.api.service;

import com.example.calendar.api.dto.EngagementMailDto;

public interface EmailService {

    void sendEngagement(EngagementMailDto engagementMailDto);
}
