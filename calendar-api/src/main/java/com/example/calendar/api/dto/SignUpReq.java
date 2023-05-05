package com.example.calendar.api.dto;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpReq {
    @Email
    @NotBlank
    private final String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private final String password;

    @NotBlank
    private final String name;

    @NotNull
    private final LocalDate birth;
}
