package com.example.house.batch.service;

import com.example.house.batch.domain.Apt;
import com.example.house.batch.domain.AptDeal;
import com.example.house.batch.domain.repository.AptDealRepository;
import com.example.house.batch.domain.repository.AptRepository;
import com.example.house.batch.dto.AptDealDto;
import com.example.house.batch.dto.AptDto;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AptService {

    private final AptRepository aptRepository;
    private final AptDealRepository aptDealRepository;

    @Transactional
    public void upsert(AptDealDto dto) {
        Apt apt = aptRepository.findByAptNameAndAddressAndProvinceAndLawdProvinceCodeAndBuiltYear(
            dto.getAptName(), dto.getAddress(), dto.getProvince(), dto.getLawdProvinceCode(),
            dto.getBuiltYear()
        ).orElseGet(() -> Apt.of(dto));

        aptRepository.save(apt);

        AptDeal aptDeal = aptDealRepository.findByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(
            apt, dto.getExclusiveArea(), dto.getDealDate(), dto.getDealAmount(), dto.getFloor()
        ).orElseGet(() -> AptDeal.of(dto));

        aptDeal.map(apt);
        aptDeal.update(dto.getDealCanceled(), dto.getDealCanceledDate());
        aptDealRepository.save(aptDeal);
    }

    @Transactional(readOnly = true)
    public List<AptDto> findAptInfo(String lawdProvinceCode, LocalDate dealDate) {
        return aptDealRepository.findAllByApt_LawdProvinceCodeAndDealCanceledIsFalseAndDealDate(lawdProvinceCode,
                                    dealDate)
                                .stream()
                                .map(aptDeal ->
                                    new AptDto(aptDeal.getApt().getAptName(), aptDeal.getDealAmount(),
                                        aptDeal.getApt().getBuiltYear(),
                                        aptDeal.getExclusiveArea()))
                                .collect(Collectors.toUnmodifiableList());
    }
}
