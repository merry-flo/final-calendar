package com.example.house.batch.job.notification;

import com.example.house.batch.domain.AptNotification;
import com.example.house.batch.domain.Lawd;
import com.example.house.batch.domain.repository.LawdRepository;
import com.example.house.batch.dto.AptDto;
import com.example.house.batch.dto.AptNotificationDto;
import com.example.house.batch.job.validator.RequestDateParameterValidator;
import com.example.house.batch.service.AptService;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AptNotificationJobConfig {

    public static final String APT_NOTIFICATION_JOB_NAME = "aptNotificationJob";
    public static final int CHUNK_SIZE = 100;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final LawdRepository lawdRepository;
    private final AptService aptService;


    @Bean
    public Job aptNotificationJob(Step aptNotificationStep) {
        return jobBuilderFactory.get(APT_NOTIFICATION_JOB_NAME)
                                .incrementer(new RunIdIncrementer())
                                .start(aptNotificationStep)
                                .validator(new RequestDateParameterValidator())
                                .build();
    }

    @Bean
    public Step aptNotificationStep(
        JpaPagingItemReader<AptNotification> aptNotificationItemReader,
        ItemProcessor<AptNotification, AptNotificationDto> aptNotificationItemProcessor,
        ItemWriter<AptNotificationDto> aptNotificationItemWriter
    ) {
        return stepBuilderFactory.get("aptNotificationStep")
                                 .<AptNotification, AptNotificationDto>chunk(CHUNK_SIZE)
                                 .reader(aptNotificationItemReader)
                                 .processor(aptNotificationItemProcessor)
                                 .writer(aptNotificationItemWriter)
                                 .build();
    }

    @Bean
    public JpaPagingItemReader<AptNotification> aptNotificationItemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<AptNotification>()
            .name("aptNotificationItemReader")
            .pageSize(CHUNK_SIZE)
            .queryString("SELECT a FROM AptNotification a WHERE a.enabled = true Order By a.aptNotificationId")
            .entityManagerFactory(entityManagerFactory)
            .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<AptNotification, AptNotificationDto> aptNotificationItemProcessor(
        @Value("#{jobParameters['requestDate']}") String requestDate
    ) {
        return item -> {
            Lawd lawd = lawdRepository.findByLawdCode(item.getLawdProvinceCode() + "00000")
                                      .orElseGet(() -> null);
            if (lawd == null) {
                return null;
            }

            String provinceName = lawd.getLawdProvince();
            List<AptDto> aptDealInfo = aptService.findAptInfo(item.getLawdProvinceCode(), LocalDate.parse(requestDate));

            return new AptNotificationDto(item.getEmail(), provinceName, aptDealInfo.size(), aptDealInfo);
        };
    }

    @Bean
    public ItemWriter<AptNotificationDto> aptNotificationItemWriter() {
        return items -> items.stream().map(AptNotificationDto::toMessage).forEach(System.out::println);
    }

}
