package com.example.calendar.core.service;

import com.example.calendar.core.domain.entity.User;
import com.example.calendar.core.domain.entity.repository.UserRepository;
import com.example.calendar.core.dto.SignUpReq;
import com.example.calendar.core.util.Encryptor;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final Encryptor encryptor;
    private final UserRepository userRepository;

    @Transactional
    public User create(SignUpReq signUpReq) {
        userRepository.findByEmail(signUpReq.getEmail())
                .ifPresent(user -> {
                    throw new RuntimeException("user already exists");
                });


        return userRepository.save(
            new User(
                signUpReq.getEmail(),
                encryptor.encrypt(signUpReq.getPassword()),
                signUpReq.getName(),
                signUpReq.getBirth()
            )
        );
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmail(email)
                             .map(user -> user.isMatch(encryptor, password) ? user : null);
    }
}
