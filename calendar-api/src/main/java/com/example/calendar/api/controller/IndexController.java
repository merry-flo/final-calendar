package com.example.calendar.api.controller;

import static com.example.calendar.api.service.LoginService.LOGIN_USER_KEY;

import com.example.calendar.core.domain.RequestReplyType;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index(Model model, HttpSession session,
        @RequestParam(required = false) String redirect) {
        model.addAttribute("isSignIn", session.getAttribute(LOGIN_USER_KEY) != null);
        model.addAttribute("redirect", redirect);
        return "index";
    }

    @GetMapping("/events/engagements/{engagementId}")
    public String updateEngagement(
        @PathVariable("engagementId") Long engagementId,
        @RequestParam("type") RequestReplyType type,
        Model model, HttpSession session) {
        model.addAttribute("isSignIn",session.getAttribute(LOGIN_USER_KEY) != null);
        model.addAttribute("updateType", type.name());
        model.addAttribute("engagementId",engagementId );
        model.addAttribute("path", "events/engagements/" + engagementId + "?type=" + type.name());
        return "update-event";
    }
}
