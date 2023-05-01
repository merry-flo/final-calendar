package com.example.calendar.api.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class SignUpReq {
    private final String email;
    private final String password;
    private final String name;
    private final LocalDate birth;
}
