package com.example.calendar.api.controller;

import static com.example.calendar.api.service.LoginService.LOGIN_USER_KEY;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/")
    public String index(Model model, HttpSession session,
        @RequestParam(required = false) String redirect) {
        model.addAttribute("isSignIn", session.getAttribute(LOGIN_USER_KEY) != null);
        model.addAttribute("redirect", redirect);
        return "index";
    }
}
