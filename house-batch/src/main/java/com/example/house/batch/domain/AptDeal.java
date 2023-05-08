package com.example.house.batch.domain;

import java.time.LocalDate;
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
public class AptDeal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptDealId;

    private Long aptId;
    private Double exclusiveArea;
    private LocalDate dealDate;
    private Long dealAmount;
    private Integer floor;
    private boolean dealCanceled;
    private LocalDate dealCanceledDate;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
