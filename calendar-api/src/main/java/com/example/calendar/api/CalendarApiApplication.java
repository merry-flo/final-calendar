package com.example.calendar.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan({"com.example.calendar.core"})
@EnableJpaRepositories({"com.example.calendar.core"})
@SpringBootApplication
public class CalendarApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalendarApiApplication.class, args);
    }
}
