package com.example.calendar.core.domain.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
public class User extends BaseEntity {
    private String name;
    private String email;
    private String password;
    private LocalDate birth;

    public User(String name, String email, String password, LocalDate birth) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birth = birth;
    }
}
