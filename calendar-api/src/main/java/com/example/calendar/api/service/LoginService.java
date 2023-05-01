package com.example.calendar.api.service;

import com.example.calendar.api.dto.LoginReq;
import com.example.calendar.api.dto.SignUpReq;
import com.example.calendar.core.domain.entity.User;
import com.example.calendar.core.service.UserService;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LoginService {

    public static final String LOGIN_USER_KEY = "USER_ID";
    private final UserService userService;

    @Transactional
    public void signUp(SignUpReq signUpReq, HttpSession session) {
        User user = userService.create(
            new com.example.calendar.core.dto.SignUpReq(
                signUpReq.getEmail(),
                signUpReq.getPassword(),
                signUpReq.getName(),
                signUpReq.getBirth()
            ));

        session.setAttribute(LOGIN_USER_KEY, user.getId());
    }

    @Transactional(readOnly = true)
    public void login(LoginReq loginReq, HttpSession session) {
        Long userId = (Long) session.getAttribute(LOGIN_USER_KEY);
        if (userId != null) {
            return;
        }

        Optional<User> userOptional = userService.findUserByEmailAndPassword(loginReq.getEmail(),
            loginReq.getPassword());

        if (userOptional.isPresent()) {
            session.setAttribute(LOGIN_USER_KEY, userOptional.get().getId());
        } else {
            throw new RuntimeException("failed to login");
        }
    }

    public void logout(HttpSession session) {
        session.removeAttribute(LOGIN_USER_KEY);
    }
}
