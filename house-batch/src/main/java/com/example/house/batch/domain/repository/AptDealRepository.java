package com.example.house.batch.domain.repository;

import com.example.house.batch.domain.Apt;
import com.example.house.batch.domain.AptDeal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AptDealRepository extends JpaRepository<AptDeal, Long> {

    Optional<AptDeal> findByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(Apt apt, Double exclusiveArea, LocalDate dealDate, Long dealAmount, Integer floor);

    @EntityGraph(attributePaths = {"apt"})
    List<AptDeal> findAllByApt_LawdProvinceCodeAndDealCanceledIsFalseAndDealDate(String lawdProvinceCode, LocalDate dealDate);
}
