package com.example.calendar.api.service;

import static com.example.calendar.api.service.LoginService.LOGIN_USER_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.example.calendar.api.dto.LoginReq;
import com.example.calendar.api.dto.SignUpReq;
import com.example.calendar.api.fixture.UserFixture;
import com.example.calendar.core.domain.entity.User;
import com.example.calendar.core.exception.CalendarException;
import com.example.calendar.core.service.UserService;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserService userService;

    @DisplayName("성공 - 유저 생성해보자")
    @Test
    void given_when_then() {
        // given
        SignUpReq signUpReq = new SignUpReq("email@gmail.com", "password", "name", LocalDate.of(1933, 1, 1));
        MockHttpSession mockSession = new MockHttpSession();

        User fixture = UserFixture.fixture();
        BDDMockito.given(userService.create(any()))
                  .willReturn(fixture);
        // when
        loginService.signUp(signUpReq, mockSession);

        // then
        assertThat(mockSession.getAttribute(LOGIN_USER_KEY))
            .isEqualTo(fixture.getId());
    }

    @DisplayName("성공 - 로그인 로그인")
    @Test
    void loginNoSession() {
        // given
        LoginReq loginReq = new LoginReq("email@gmail.com", "password");
        MockHttpSession mockSession = new MockHttpSession();
        User fixture = UserFixture.fixture();
        BDDMockito.given(userService.findUserByEmailAndPassword(any(), any()))
                  .willReturn(Optional.of(fixture));

        // when
        loginService.login(loginReq, mockSession);

        // then
        assertThat(mockSession.getAttribute(LOGIN_USER_KEY))
            .isEqualTo(fixture.getId());
    }

    @DisplayName("성공 - 로그인 세션 ")
    @Test
    void loginHasSession() {
        // given
        LoginReq loginReq = new LoginReq("email@gmail.com", "password");
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(LOGIN_USER_KEY, 1L);

        // when
        loginService.login(loginReq, mockSession);

        // then
        BDDMockito.verify(userService, BDDMockito.never())
                  .findUserByEmailAndPassword(any(), any());
    }


    @DisplayName("실패 - 로그인 ")
    @Test
    void fail_login() {
        // given
        LoginReq loginReq = new LoginReq("email@gmail.com", "password");
        MockHttpSession mockSession = new MockHttpSession();
        BDDMockito.given(userService.findUserByEmailAndPassword(any(), any()))
                  .willReturn(Optional.empty());

        // when && then
        assertThatThrownBy(
               () -> loginService.login(loginReq, mockSession))
                                       .isInstanceOf(CalendarException.class);
    }

    @DisplayName("성공 - 로그아웃 ")
    @Test
    void logout() {
        // given
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(LOGIN_USER_KEY, 1L);
        assertThat(mockSession.getAttribute(LOGIN_USER_KEY))
                                       .isEqualTo(1L);

        // when
        loginService.logout(mockSession);

        // then
        assertThat(mockSession.getAttribute(LOGIN_USER_KEY))
            .isNull();
    }
}