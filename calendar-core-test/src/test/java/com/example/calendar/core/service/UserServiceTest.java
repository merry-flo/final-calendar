package com.example.calendar.core.service;

import static org.mockito.ArgumentMatchers.anyString;

import com.example.calendar.core.domain.entity.User;
import com.example.calendar.core.domain.entity.repository.UserRepository;
import com.example.calendar.core.dto.SignUpReq;
import com.example.calendar.core.util.Encryptor;
import java.time.LocalDate;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private Encryptor encryptor;
    @Mock
    private UserRepository userRepository;

    @DisplayName("유저 생성 목 테스트")
    @Test
    void given_when_then() {
        // given
        SignUpReq signUpReq = new SignUpReq("test@gmail.com", "password", "안세형", LocalDate.of(1993, 1, 8));
        BDDMockito.given(userRepository.findByEmail(anyString()))
                  .willReturn(Optional.empty());
        String encryptedPassword = "encryptedPassword";
        BDDMockito.given(encryptor.encrypt(anyString()))
                  .willReturn(encryptedPassword);
        BDDMockito.given(userRepository.save(BDDMockito.any()))
                  .willReturn(
                      new User(signUpReq.getEmail(), encryptedPassword, signUpReq.getName(), signUpReq.getBirth()));

        // when
        User createdUser = userService.create(signUpReq);

        // then
        Assertions.assertThat(createdUser.getEmail()).isEqualTo(signUpReq.getEmail());
        Assertions.assertThat(createdUser.getPassword()).isEqualTo(encryptedPassword);
    }
}