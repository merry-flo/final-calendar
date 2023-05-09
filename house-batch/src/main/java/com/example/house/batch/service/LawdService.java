package com.example.house.batch.service;

import com.example.house.batch.domain.Lawd;
import com.example.house.batch.domain.repository.LawdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LawdService {
    private final LawdRepository lawdRepository;

    @Transactional
    public void upsert(Lawd lawd) {
        // 데이터 존재하면 수정, 없으면 생성
        Lawd savedLawd = lawdRepository.findByLawdCode(lawd.getLawdCode())
                                   .orElseGet(Lawd::new);
        savedLawd.update(lawd.getLawdCode(), lawd.getLawdProvince(), lawd.getExist());
        lawdRepository.save(savedLawd);
    }
}
