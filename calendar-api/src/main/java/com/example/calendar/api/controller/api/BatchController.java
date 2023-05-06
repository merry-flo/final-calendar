package com.example.calendar.api.controller.api;

import com.example.calendar.api.dto.SendMailBatchReq;
import com.example.calendar.api.service.EmailService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BatchController {

    private final EmailService emailService;

    @PostMapping("/api/batch/mail")
    public ResponseEntity<Void> sendMail(@RequestBody List<SendMailBatchReq> reqs) {
        reqs.forEach(r -> emailService.sendAlarmMail(r));
        return ResponseEntity.ok().build();
    }
}
