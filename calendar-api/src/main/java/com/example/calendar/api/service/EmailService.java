package com.example.calendar.api.service;

import com.example.calendar.core.domain.entity.Engagement;

public interface EmailService {

    void sendEngagement(Engagement engagement);
}
