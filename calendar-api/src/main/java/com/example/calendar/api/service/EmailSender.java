package com.example.calendar.api.service;

import com.example.calendar.api.dto.EngagementMailDto;
import com.example.calendar.api.dto.SendMailBatchReq;

public interface EmailSender {

    void sendEngagement(EngagementMailDto engagementMailDto);

    void sendAlarmMail(SendMailBatchReq req);
}
