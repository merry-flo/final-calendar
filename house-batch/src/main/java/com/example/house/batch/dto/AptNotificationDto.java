package com.example.house.batch.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AptNotificationDto {
    private String email;
    private String provinceName;
    private Integer count;
    private List<AptDto> aptList;

    public String toMessage() {
        return String.format(
            "안녕하세요. %s 고객님!\n%s에 %d개의 아파트 거래 정보가 등록되었습니다.\n%s\n========== fin", email, provinceName, count,
            aptList.stream().map(AptDto::toString).collect(Collectors.joining("\n")));
    }
}
