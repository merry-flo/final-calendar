package com.example.house.batch.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Apt extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptId;

    @Column(length = 40)
    private String aptName;

    @Column(length = 20)
    private String address;

    @Column(length = 40)
    private String province;

    @Column(length = 5)
    private String lawdProvinceCode;

    private Integer builtYear;

}
