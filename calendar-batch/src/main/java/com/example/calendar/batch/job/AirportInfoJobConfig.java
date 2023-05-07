package com.example.calendar.batch.job;

import com.example.calendar.batch.dto.AirportInfoDto;
import com.example.calendar.batch.dto.AirportKoInfoDto;
import com.example.calendar.batch.job.mapper.AirportInfoFieldSetMapper;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

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
        ItemReader<AirportInfoDto> airportInfoReader,
        ItemProcessor<AirportInfoDto, AirportKoInfoDto> airportInfoProcessor,
        ItemWriter<AirportKoInfoDto> airportInfoWriter
    ) {
        return stepBuilderFactory.get("airportInfoStep")
                                 .<AirportInfoDto, AirportKoInfoDto>chunk(20)
                                 .reader(airportInfoReader)
                                 .processor(airportInfoProcessor)
                                 .writer(airportInfoWriter)
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


    @StepScope
    @Bean
    public ItemProcessor<AirportInfoDto, AirportKoInfoDto> airportInfoProcessor() {
        return item -> {
            AirportKoInfoDto airportKoInfoDto = new AirportKoInfoDto();
            airportKoInfoDto.setAirportName(item.getAirportNameKo());
            airportKoInfoDto.setCountry(item.getCountryKo());
            airportKoInfoDto.setLocation(item.getLocationKo());
            airportKoInfoDto.setLatitude(item.getLatitude());
            airportKoInfoDto.setLongitude(item.getLongitude());
            return airportKoInfoDto;
        };
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<AirportKoInfoDto> airportInfoWriter() throws IOException {

        BeanWrapperFieldExtractor<AirportKoInfoDto> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(
            new String[]{"airportName", "country", "location", "latitude", "longitude"});
            fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<AirportKoInfoDto> airportInfoAggregator = new DelimitedLineAggregator<>();
        airportInfoAggregator.setDelimiter(",");
        airportInfoAggregator.setFieldExtractor(fieldExtractor);

        String path = "airport_info_ko.csv";
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }

        Resource resource = new FileSystemResource(path);
        return new FlatFileItemWriterBuilder<AirportKoInfoDto>()
            .name("airportInfoWriter")
            .lineAggregator(airportInfoAggregator)
            .resource(resource)
            .build();
    }

}

