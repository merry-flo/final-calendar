package com.example.calendar.api.dto;

import com.example.calendar.core.domain.RequestReplyType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyEngagementReq {
    private RequestReplyType type;
}
