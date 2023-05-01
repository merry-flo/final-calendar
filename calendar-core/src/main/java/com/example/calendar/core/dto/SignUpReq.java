package com.example.calendar.core.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpReq {
    private final String email;
    private final String password;
    private final String name;
    private final LocalDate birth;
}
