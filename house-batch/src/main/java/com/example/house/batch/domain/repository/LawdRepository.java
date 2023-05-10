package com.example.house.batch.domain.repository;

import com.example.house.batch.domain.Lawd;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LawdRepository extends JpaRepository<Lawd, Long> {

    Optional<Lawd> findByLawdCode(String lawdCode);

    @Query(" SELECT DISTINCT substring(l.lawdCode, 1, 5)  "
        + " FROM Lawd l "
        + " where l.exist = true and l.lawdCode not like '%00000000'")
    List<String> findAllDistinctLawdProvinceCode();
}
