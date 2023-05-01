package com.example.calendar.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages ={"com.example.calendar"})
public class CalendarApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalendarApiApplication.class, args);
    }
}
