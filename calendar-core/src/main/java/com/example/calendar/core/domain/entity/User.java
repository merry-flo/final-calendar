package com.example.calendar.core.domain.entity;

import com.example.calendar.core.util.Encryptor;
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
    private String email;
    private String password;
    private String name;
    private LocalDate birth;

    public User(String email, String password, String name, LocalDate birth) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birth = birth;
    }
    public boolean isMatch(Encryptor encryptor, String password) {
        return encryptor.isMatch(password, this.password);
    }

}
