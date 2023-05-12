package com.example.house.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AptDto {
    private String name;
    private Long dealAmount;
    private Integer builtYear;
    private Double exclusiveArea;

    @Override
    public String toString() {
        return String.format(
            "- %s : %,d 원 (건축년도 : %d 년, 전용면적 : %.2f m3)", name, dealAmount * 10000, builtYear, exclusiveArea);
    }

}
