package com.example.calendar.batch.parallel;

import com.example.calendar.batch.dto.AirportInfoDto;
import com.example.calendar.batch.job.mapper.AirportInfoFieldSetMapper;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
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
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

/*
    multi threaded step 은 단일 프로세스에서 chunk 단위의 병렬처리를 task executor 를 통한 멀티 스레드로 지원한다.
 */
@Configuration
@RequiredArgsConstructor
public class MultiThreadedStepJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AtomicLong atomicLong = new AtomicLong(0);

    @Bean
    public Job multiThreadedStepJob(Step multiThreadedStep){
        return jobBuilderFactory.get("multiThreadedStepJob")
                                .incrementer(new RunIdIncrementer())
                                .start(multiThreadedStep)
                                .build();

    }

    @JobScope
    @Bean
    public Step multiThreadedStep(
        ItemReader<AirportInfoDto> multiThreadAirportInfoReader,
        ItemProcessor<AirportInfoDto, AirportInfoDto> multiThreadAirportInfoProcessor,
        ItemWriter<AirportInfoDto> multiThreadAirportInfoWriter,
        TaskExecutor taskExecutor
    ) {
        return stepBuilderFactory.get("multiThreadedStep")
                                 .<AirportInfoDto, AirportInfoDto>chunk(20)
                                 .reader(multiThreadAirportInfoReader)
                                 .processor(multiThreadAirportInfoProcessor)
                                 .writer(multiThreadAirportInfoWriter)
                                 .taskExecutor(taskExecutor)
                                 .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(
            "sprint-batch-multi-threaded-step-executor");
        taskExecutor.setConcurrencyLimit(4);
        return taskExecutor;
    }

    @StepScope
    @Bean
    public FlatFileItemReader<AirportInfoDto> multiThreadAirportInfoReader() {
        return new FlatFileItemReaderBuilder<AirportInfoDto>()
            .name("airportInfoReader")
            .lineTokenizer(new DelimitedLineTokenizer())
            .fieldSetMapper(new AirportInfoFieldSetMapper())
            .resource(new ClassPathResource("static/airport_info.csv"))
            .build();
    }


    @StepScope
    @Bean
    public ItemProcessor<AirportInfoDto, AirportInfoDto> multiThreadAirportInfoProcessor() {
        return item -> {
            item.setId(atomicLong.addAndGet(1L));
            return item;
        };
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<AirportInfoDto> multiThreadAirportInfoWriter() throws IOException {
        BeanWrapperFieldExtractor<AirportInfoDto> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"id", "airportNameKo"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<AirportInfoDto> airportInfoAggregator = new DelimitedLineAggregator<>();
        airportInfoAggregator.setDelimiter(",");
        airportInfoAggregator.setFieldExtractor(fieldExtractor);

        String path = "airport_info_output.csv";
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }

        Resource resource = new FileSystemResource(path);
        return new FlatFileItemWriterBuilder<AirportInfoDto>()
            .name("airportInfoWriter")
            .lineAggregator(airportInfoAggregator)
            .resource(resource)
            .build();
    }
}
