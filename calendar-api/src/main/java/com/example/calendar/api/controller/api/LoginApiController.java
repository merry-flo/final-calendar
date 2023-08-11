package com.example.calendar.api.controller.api;

import com.example.calendar.api.dto.LoginReq;
import com.example.calendar.api.dto.SignUpReq;
import com.example.calendar.api.service.LoginService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginApiController {

    private final LoginService loginService;

    @PostMapping("/api/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpReq req, HttpSession session) {
        loginService.signUp(req, session);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginReq req, HttpSession session) {
        loginService.login(req, session);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        loginService.logout(session);
        return ResponseEntity.ok().build();
    }
}
