package com.example.calendar.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class CalendarBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalendarBatchApplication.class, args);
    }
}
