package com.example.house.batch.config;

import com.example.house.batch.service.NotificationService;
import com.example.house.batch.service.PrintNotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AptBatchBeanConfig {

    @Bean
    public NotificationService notificationService() {
        return new PrintNotificationService();
    }
}
