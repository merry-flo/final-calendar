package com.example.calendar.api.fixture;

import com.example.calendar.core.domain.entity.User;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class UserFixture {
    public static User fixture() {
        User fixture = fixture("test@gmail.com", "password", "test", LocalDate.of(1993, 1, 1));
        fixture.setId(1L);
        return fixture;
    }

    public static User fixture(String email, String password, String name, LocalDate birth) {
        return new User(email, password, name, birth);
    }

    public static List<User> fixtures() {
        return IntStream.range(1, 4).mapToObj(
                            i -> fixture("test" + i + "@gmail.com", "password", "test" + i, LocalDate.of(1993, 1, 1)))
                        .collect(Collectors.toList());
    }

}
