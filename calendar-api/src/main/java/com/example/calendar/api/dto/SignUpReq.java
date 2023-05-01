package com.example.calendar.api.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class SignUpReq {
    private String email;
    private String password;
    private String name;
    private LocalDate birth;
}
