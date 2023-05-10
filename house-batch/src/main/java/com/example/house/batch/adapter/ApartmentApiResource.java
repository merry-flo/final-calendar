package com.example.house.batch.adapter;

import java.net.MalformedURLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

@Component
public class ApartmentApiResource {

    @Value("${apartment.api.path}")
    private String path;

    @Value("${apartment.api.serviceKey}")
    private String serviceKey;


    public Resource getResource(String lawdCode, YearMonth yearMonth)  {
        String url = String.format("%s?serviceKey=%s&LAWD_CD=%s&DEAL_YMD=%s", path, serviceKey, lawdCode,
            yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM")));
        try {
            return new UrlResource(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("url 생성에 실패하였습니다.");
        }
    }
}
