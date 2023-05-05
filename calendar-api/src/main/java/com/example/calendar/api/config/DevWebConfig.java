package com.example.calendar.api.config;

import com.example.calendar.api.service.EmailService;
import com.example.calendar.api.service.FakeEmailService;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile("dev")
@Configuration
public class DevWebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new FakeUserAuthArgumentResolver());
    }
}
