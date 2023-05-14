package com.example.calendar.api.service;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import com.example.calendar.api.dto.EngagementMailDto;
import com.example.calendar.api.dto.SendMailBatchReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Profile("test")
@Component
public class FakeEmailSender implements EmailSender {

    @Async("calendar-api-mail-sender")
    @TransactionalEventListener(
        phase = AFTER_COMMIT,
        classes = EngagementMailDto.class
    )
    @Override
    public void sendEngagement(EngagementMailDto engagementMailDto) {
        log.info("[{}] Send email to {}", Thread.currentThread().getName(), engagementMailDto.toString());
    }

    @Override
    public void sendAlarmMail(SendMailBatchReq req) {
        log.info("Send alarm email to {}", req.toString());
    }
}
