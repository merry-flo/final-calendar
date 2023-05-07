package com.example.calendar.batch.job.mapper;

import com.example.calendar.batch.dto.AirportInfoDto;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class AirportInfoFieldSetMapper implements FieldSetMapper<AirportInfoDto> {
    @Override
    public AirportInfoDto mapFieldSet(FieldSet fieldSet) throws BindException {
        AirportInfoDto airportInfoDto = new AirportInfoDto();
        airportInfoDto.setAirportNameEn(fieldSet.readString(0));
        airportInfoDto.setAirportNameKo(fieldSet.readString(1));
        airportInfoDto.setIataCode(fieldSet.readString(2));
        airportInfoDto.setIcaoCode(fieldSet.readString(3));
        airportInfoDto.setLocationKo(fieldSet.readString(4));
        airportInfoDto.setLocationEn(fieldSet.readString(5));
        airportInfoDto.setCountryEn(fieldSet.readString(6));
        airportInfoDto.setCountryKo(fieldSet.readString(7));
        airportInfoDto.setCityEn(fieldSet.readString(8));
        airportInfoDto.setLatitude(fieldSet.readString(9));
        airportInfoDto.setLongitude(fieldSet.readString(10));

        return airportInfoDto;
    }
}
