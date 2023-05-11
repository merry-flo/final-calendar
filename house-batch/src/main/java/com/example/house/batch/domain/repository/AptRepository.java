package com.example.house.batch.domain.repository;

import com.example.house.batch.domain.Apt;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AptRepository extends JpaRepository<Apt, Long> {

    Optional<Apt> findByAptNameAndAddressAndProvinceAndLawdProvinceCodeAndBuiltYear(String aptName, String address, String province, String lawdProvinceCode, Integer builtYear);

}
