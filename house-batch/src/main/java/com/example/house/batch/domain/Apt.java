package com.example.house.batch.domain;

import com.example.house.batch.dto.AptDealDto;
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

    public static Apt of(AptDealDto dto) {
        Apt apt = new Apt();
        apt.aptName = dto.getAptName();
        apt.address = dto.getAddress();
        apt.province = dto.getProvince();
        apt.lawdProvinceCode = dto.getLawdProvinceCode().strip();
        apt.builtYear = dto.getBuiltYear();
        return apt;
    }

}
