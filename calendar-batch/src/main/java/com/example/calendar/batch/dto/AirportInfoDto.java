package com.example.calendar.batch.dto;

import lombok.Data;

@Data
public class AirportInfoDto {
    private String airportNameEn;
    private String airportNameKo;
    private String iataCode;
    private String icaoCode;
    private String locationKo;;
    private String locationEn;
    private String countryEn;
    private String countryKo;
    private String cityEn;
    private String latitude;
    private String longitude;
}