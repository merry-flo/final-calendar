package com.example.calendar.batch.job;

import com.example.calendar.batch.dto.AirportEnInfoDto;
import com.example.calendar.batch.dto.AirportInfoDto;
import com.example.calendar.batch.job.mapper.AirportInfoFieldSetMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AirportInfoJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job airportInfoJob(Step airportInfoStep) {
        return jobBuilderFactory.get("airportInfoJob")
                                .incrementer(new RunIdIncrementer())
                                .start(airportInfoStep)
                                .build();
    }


    @JobScope
    @Bean
    public Step airportInfoStep(
        ItemReader<AirportInfoDto> airportInfoReader
    ) {
        return stepBuilderFactory.get("airportInfoStep")
                                 .<AirportInfoDto, AirportEnInfoDto>chunk(20)
                                 .reader(airportInfoReader)
                                 .processor(
                                     new ItemProcessor<AirportInfoDto, AirportEnInfoDto>() {
                                         @Override
                                         public AirportEnInfoDto process(AirportInfoDto item) throws Exception {
                                             return new AirportEnInfoDto();
                                         }
                                     }
                                 )
                                 .writer(
                                     item -> System.out.println(item)
                                 )
                                 .allowStartIfComplete(true)
                                 .build();
    }

    @StepScope
    @Bean
    public FlatFileItemReader<AirportInfoDto> airportInfoReader() {
        return new FlatFileItemReaderBuilder<AirportInfoDto>()
            .name("airportInfoReader")
            .lineTokenizer(new DelimitedLineTokenizer())
            .fieldSetMapper(new AirportInfoFieldSetMapper())
            .resource(new ClassPathResource("static/airport_info.csv"))
            .build();
    }

}

