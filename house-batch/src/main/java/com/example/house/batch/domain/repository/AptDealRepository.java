package com.example.house.batch.domain.repository;

import com.example.house.batch.domain.Apt;
import com.example.house.batch.domain.AptDeal;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AptDealRepository extends JpaRepository<AptDeal, Long> {

    Optional<AptDeal> findByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(Apt apt, Double exclusiveArea, LocalDate dealDate, Long dealAmount, Integer floor);
}
