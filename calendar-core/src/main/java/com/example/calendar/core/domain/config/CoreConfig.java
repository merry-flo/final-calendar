package com.example.calendar.core.domain.config;

import com.example.calendar.core.util.BCryptEncryptor;
import com.example.calendar.core.util.Encryptor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EntityScan({"com.example.calendar.core"})
@EnableJpaRepositories({"com.example.calendar.core"})
@Configuration
public class CoreConfig {

    @Bean
    public Encryptor bcryptEncryptor() {
        return new BCryptEncryptor();
    }

}