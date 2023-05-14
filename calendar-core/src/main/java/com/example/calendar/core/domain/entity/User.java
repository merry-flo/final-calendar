package com.example.calendar.core.domain.entity;

import com.example.calendar.core.util.Encryptor;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        if (user.getId() == null) {
            return Objects.equals(this.email, user.getEmail());
        }
        return Objects.equals(super.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Objects.requireNonNullElseGet(super.getId(), () -> this.email));
    }

}
