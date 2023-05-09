package com.example.house.batch.job.lawd;

import com.example.house.batch.domain.Lawd;
import com.example.house.batch.job.validator.FilePathParameterValidator;
import com.example.house.batch.service.LawdService;
import java.util.List;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class LawdInsertJobConfig {

    public static final String JOB_NAME = "lawdInsertJob";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final LawdService lawdService;

    @Bean
    public Job lawdInsertJob(Step lawdInsertStep) {
        return jobBuilderFactory.get(JOB_NAME)
                                .incrementer(new RunIdIncrementer())
                                .validator(new FilePathParameterValidator())
                                .start(lawdInsertStep)
                                .build();
    }

    @JobScope
    @Bean
    public Step lawdInsertStep(
        FlatFileItemReader<Lawd> lawdFileItemReader,
        ItemWriter<Lawd> lawdItemWriter
    ) {
        return stepBuilderFactory.get("lawdInsertStep")
                                 .<Lawd, Lawd>chunk(1000)
                                 .reader(lawdFileItemReader)
                                 .writer(lawdItemWriter)
                                 .build();
    }

    @StepScope
    @Bean
    public FlatFileItemReader<Lawd> lawdFileItemReader(@Value("#{jobParameters['filePath']}") String filePath) {
        return new FlatFileItemReaderBuilder<Lawd>()
            .name("lawdFileItemReader")
            .delimited()
            .delimiter("\t")
            .names(LawdFieldSetMapper.LAWD_CODE, LawdFieldSetMapper.LAWD_PROVINCE, LawdFieldSetMapper.EXIST)
            .linesToSkip(1)
            .fieldSetMapper(new LawdFieldSetMapper())
            .resource(new ClassPathResource(filePath))
            .build();
    }

    @StepScope
    @Bean
    public ItemWriter<Lawd> lawdItemWriter() {
        return items -> items.forEach(lawdService::upsert);
    }

}
