package com.example.calendar.api.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginReq {
    @Email
    @NotBlank
    private final String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private final String password;
}
