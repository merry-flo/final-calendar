package com.example.calendar.api.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendMailBatchReq {
    private Long id;
    private LocalDateTime startAt;
    private String title;
    private String email;
}