package com.example.house.batch.domain;

import com.example.house.batch.dto.AptDealDto;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AptDeal extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptDealId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apt_id")
    private Apt apt;

    private Double exclusiveArea;
    private LocalDate dealDate;
    private Long dealAmount;
    private Integer floor;
    private boolean dealCanceled;
    private LocalDate dealCanceledDate;

    public static AptDeal of(AptDealDto dto) {
        AptDeal aptDeal = new AptDeal();
        aptDeal.exclusiveArea = dto.getExclusiveArea();
        aptDeal.dealDate = dto.getDealDate();
        aptDeal.dealAmount = dto.getDealAmount();
        aptDeal.floor = dto.getFloor();
        aptDeal.dealCanceled = dto.getDealCanceled();
        aptDeal.dealCanceledDate = dto.getDealCanceledDate();
        return aptDeal;
    }

    public void update(boolean dealCanceled, LocalDate dealCanceledDate) {
        this.dealCanceled = dealCanceled;
        this.dealCanceledDate = dealCanceledDate;
    }

    public void map(Apt apt) {
        this.apt = apt;
    }
}
