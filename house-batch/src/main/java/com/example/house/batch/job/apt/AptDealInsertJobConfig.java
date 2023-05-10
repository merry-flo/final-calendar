package com.example.house.batch.job.apt;

import com.example.house.batch.adapter.ApartmentApiResource;
import com.example.house.batch.domain.repository.LawdRepository;
import com.example.house.batch.dto.AptDealDto;
import com.example.house.batch.job.validator.LawdCodeParameterValidator;
import com.example.house.batch.job.validator.YearMonthParameterValidator;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AptDealInsertJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApartmentApiResource apartmentApiResource;
    private final LawdRepository lawdRepository;

    public static final String JOB_NAME = "aptDealInsertJob";

    @Bean
    public Job aptDealInsertJob(
        Step lawdProvinceCodeStep,
        Step executionContextPrintStep
    ) {
        return jobBuilderFactory.get(JOB_NAME)
                                .incrementer(new RunIdIncrementer())
                                .start(lawdProvinceCodeStep)
                                .next(executionContextPrintStep)
//                                .validator(aptDealJobParameterValidator())
                                .build();
    }

    private CompositeJobParametersValidator aptDealJobParameterValidator() {
        return new CompositeJobParametersValidator() {{
            setValidators(
                List.of(new YearMonthParameterValidator(), new LawdCodeParameterValidator())
            );
        }};
    }

    @JobScope
    @Bean
    public Step lawdProvinceCodeStep() {
        return stepBuilderFactory.get("lawdProvinceCodeStep")
                                 .tasklet((contribution, chunkContext) -> {
                                     StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
                                     ExecutionContext executionContext = stepExecution.getJobExecution()
                                                                                      .getExecutionContext();
                                     List<String> lawdProvinceCode = lawdRepository.findAllDistinctLawdProvinceCode();

                                     executionContext.put("lawdProvinceCode", String.join(",", lawdProvinceCode));
                                     return RepeatStatus.FINISHED;
                                 })
                                 .build();
    }

    @JobScope
    @Bean
    public Step executionContextPrintStep(
        @Value("#{jobExecutionContext['lawdProvinceCode']}") String lawdProvinceCode
    ) {
        return stepBuilderFactory.get("executionContextPrintStep")
                                 .tasklet((contribution, chunkContext) -> {
                                     System.out.println("lawdProvinceCode = " + lawdProvinceCode);
                                     return RepeatStatus.FINISHED;
                                 })
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
        @Value("#{jobParameters['yearMonth']}") String yearMonth,
        @Value("#{jobParameters['lawdCode']}") String lawdCode,
        Jaxb2Marshaller aptDealDtoMarshaller
    ) {
        return new StaxEventItemReaderBuilder<AptDealDto>()
            .name("aptDealResourceReader")
            .resource(apartmentApiResource.getResource(lawdCode, YearMonth.parse(yearMonth)))
            .addFragmentRootElements("item")
            .unmarshaller(aptDealDtoMarshaller)
            .build();
    }

    @Bean
    public Jaxb2Marshaller aptDealDtoMarshaller() {
        return new Jaxb2Marshaller() {{
            setClassesToBeBound(AptDealDto.class);
        }};
    }

    @Bean
    public ItemWriter<AptDealDto> aptDealItemWriter() {
        return items -> {
            for (AptDealDto item : items) {
                log.info("item: {}", item);
            }
        };
    }
}
