package com.example.house.batch.domain.repository;

import com.example.house.batch.domain.Lawd;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LawdRepository extends JpaRepository<Lawd, Long> {

    Optional<Lawd> findByLawdCode(String lawdCode);

}
