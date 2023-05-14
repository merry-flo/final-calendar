package com.example.house.batch.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_apt_notification_email_lawd_province_code",
        columnNames = {"email", "lawdProvinceCode"}
    )
})
@Getter
@NoArgsConstructor
@Entity
public class AptNotification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptNotificationId;

    private String email;

    @Column(length = 5)
    private String lawdProvinceCode;

    private boolean enabled;

    @Builder
    public AptNotification(String email, String lawdProvinceCode, boolean enabled) {
        this.email = email;
        this.lawdProvinceCode = lawdProvinceCode;
        this.enabled = enabled;
    }
}