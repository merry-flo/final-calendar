package com.example.house.batch.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.ToString;
import org.springframework.util.StringUtils;

@ToString
@XmlRootElement(name = "item")
public class AptDealDto {

    @XmlElement(name = "거래금액")
    private String dealAmount;

    @XmlElement(name = "건축년도")
    private Integer builtYear;

    @XmlElement(name = "년")
    private Integer year;

    @XmlElement(name = "법정동")
    private String province;

    @XmlElement(name = "아파트")
    private String aptName;

    @XmlElement(name = "월")
    private Integer month;

    @XmlElement(name = "일")
    private Integer day;

    @XmlElement(name = "전용면적")
    private Double exclusiveArea;

    @XmlElement(name = "지번")
    private String address;

    @XmlElement(name = "지역코드")
    private String lawdProvinceCode;

    @XmlElement(name = "층")
    private Integer floor;

    @XmlElement(name = "해제사유발생일")
    private String dealCanceledDate;

    @XmlElement(name = "해제여부")
    private String dealCanceled;

    public String getAptName() {
        return nullSafeValue(this.aptName);
    }

    public String getAddress() {
        return nullSafeValue(this.address);
    }

    public String getProvince() {
        return nullSafeValue(this.province);
    }

    public String getLawdProvinceCode() {
        return nullSafeValue(this.lawdProvinceCode);
    }

    public Integer getBuiltYear() {
        return this.builtYear;
    }

    private String nullSafeValue(String value) {
        return Objects.requireNonNullElseGet(value, () -> "")
                      .strip();
    }

    public Double getExclusiveArea() {
        return this.exclusiveArea;
    }

    public Long getDealAmount() {
        return Long.parseLong(
            nullSafeValue(this.dealAmount)
                .replace(",", ""));
    }

    public Integer getFloor() {
        return this.floor;
    }

    public boolean getDealCanceled() {
        return "O".equals(nullSafeValue(this.dealCanceled));
    }

    public LocalDate getDealCanceledDate() {
        return StringUtils.hasText(this.dealCanceledDate)
            ? LocalDate.parse(
            nullSafeValue(this.dealCanceledDate),
            DateTimeFormatter.ofPattern("yy.MM.dd")) : null;
    }

    public LocalDate getDealDate() {
        return LocalDate.of(this.year, this.month, this.day);
    }

}
