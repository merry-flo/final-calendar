package com.example.house.batch.job.apt;

import com.example.house.batch.domain.AptDeal;
import com.example.house.batch.dto.AptDealDto;
import com.example.house.batch.job.validator.FilePathParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AptDealInsertJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final String JOB_NAME = "aptDealInsertJob";

    @Bean
    public Job aptDealInsertJob(
        Step aptDealInsertStep
    ) {
        return jobBuilderFactory.get(JOB_NAME)
                                .incrementer(new RunIdIncrementer())
                                .start(aptDealInsertStep)
                                .validator(new FilePathParameterValidator())
                                .build();
    }

    @JobScope
    @Bean
    public Step aptDealInsertStep(
        StaxEventItemReader<AptDealDto> aptDealResourceReader,
        ItemWriter<AptDealDto> aptDealItemWriter
    ) {
        return stepBuilderFactory.get("aptDealInsertStep")
                                 .<AptDealDto, AptDealDto>chunk(10)
                                 .reader(aptDealResourceReader)
                                 .writer(aptDealItemWriter)
                                 .build();
    }

    @StepScope
    @Bean
    public StaxEventItemReader<AptDealDto> aptDealResourceReader(
        @Value("#{jobParameters['filePath']}") String filePath,
        Jaxb2Marshaller aptDealDtoMarshaller
    ) {
        return new StaxEventItemReaderBuilder<AptDealDto>()
            .name("aptDealResourceReader")
            .resource(new ClassPathResource(filePath))
            .addFragmentRootElements("item")
            .unmarshaller(aptDealDtoMarshaller)
            .build();
    }

    @StepScope
    @Bean
    public Jaxb2Marshaller aptDealDtoMarshaller() {
        return new Jaxb2Marshaller() {{
            setClassesToBeBound(AptDealDto.class);
        }};
    }

    @StepScope
    @Bean
    public ItemWriter<AptDealDto> aptDealItemWriter() {
        return items -> {
            for (AptDealDto item : items) {
                log.info("item: {}", item);
            }
        };
    }
}
