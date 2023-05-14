package com.example.calendar.api.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public class ThreadPoolConfig {


    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10, new CustomizableThreadFactory("calendar-api-mail-sender"));
    }
}
