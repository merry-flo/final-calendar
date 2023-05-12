package com.example.house.batch.job.apt;

import com.example.house.batch.adapter.ApartmentApiResource;
import com.example.house.batch.domain.repository.LawdRepository;
import com.example.house.batch.dto.AptDealDto;
import com.example.house.batch.job.validator.LawdCodeParameterValidator;
import com.example.house.batch.job.validator.YearMonthParameterValidator;
import com.example.house.batch.service.AptService;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AptDealInsertJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApartmentApiResource apartmentApiResource;
    private final AptService aptService;

    public static final String JOB_NAME = "aptDealInsertJob";

    /*
        conditional step flow 를 활용해 step 의 반복적인 처리
     */
    @Bean
    public Job aptDealInsertJob(
        Step lawdProvinceCodeStep,
        Step aptDealInsertStep
    ) {
        return jobBuilderFactory.get(JOB_NAME)
                                .incrementer(new RunIdIncrementer())
                                .start(lawdProvinceCodeStep)
                                .on("CONTINUABLE").to(aptDealInsertStep).next(lawdProvinceCodeStep)
                                .on("*").end()
                                .end()
                                .build();
    }

    @JobScope
    @Bean
    public Step lawdProvinceCodeStep(Tasklet lawdProvinceCodeTasklet) {
        return stepBuilderFactory.get("lawdProvinceCodeStep")
                                 .tasklet(lawdProvinceCodeTasklet)
                                 .build();
    }

    @StepScope
    @Bean
    public Tasklet lawdProvinceCodeTasklet(LawdRepository lawdRepository) {
        return new LawdProvinceCodeTasklet(lawdRepository);
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

    @Profile("!test")
    @StepScope
    @Bean
    public StaxEventItemReader<AptDealDto> aptDealResourceReader(
        @Value("#{jobParameters['yearMonth']}") String yearMonth,
        @Value("#{jobExecutionContext['lawdProvinceCode']}") String lawdProvinceCode,
        Jaxb2Marshaller aptDealDtoMarshaller
    ) {
        return new StaxEventItemReaderBuilder<AptDealDto>()
            .name("aptDealResourceReader")
            .resource(apartmentApiResource.getResource(lawdProvinceCode, YearMonth.parse(yearMonth)))
            .addFragmentRootElements("item")
            .unmarshaller(aptDealDtoMarshaller)
            .build();
    }

    @Bean
    public ItemWriter<AptDealDto> aptDealItemWriter() {
        return items -> items.forEach(aptService::upsert);
    }

    @Bean
    public Jaxb2Marshaller aptDealDtoMarshaller() {
        return new Jaxb2Marshaller() {{
            setClassesToBeBound(AptDealDto.class);
        }};
    }

    @Profile("test")
    @StepScope
    @Bean
    public StaxEventItemReader<AptDealDto> aptDealResourceReader(
        @Value("#{jobExecutionContext['lawdProvinceCode']}") String lawdProvinceCode,
        Jaxb2Marshaller aptDealDtoMarshaller
    ) {
        return new StaxEventItemReaderBuilder<AptDealDto>()
            .name("aptDealResourceReader")
            .resource(new ClassPathResource("apartment_api_response.xml"))
            .addFragmentRootElements("item")
            .unmarshaller(aptDealDtoMarshaller)
            .build();
    }


    @Profile("test")
    @JobScope
    @Bean
    public Step executionContextPrintStep(
        Tasklet executionContextPrintTasklet
    ) {
        return stepBuilderFactory.get("executionContextPrintStep")
                                 .tasklet(executionContextPrintTasklet)
                                 .build();
    }


    /*
    JobExecutionContext 를 JobScope 의 Step 에서 가지고 오는 경우
    최초에 Step 이 초기화 될때 값이 할당되고 그 이후에는 값이 변경되지 않는다.
    */

    @Profile("test")
    @StepScope
    @Bean
    public Tasklet executionContextPrintTasklet(
        @Value("#{jobExecutionContext['lawdProvinceCode']}") String lawdProvinceCode
    ) {
        return (contribution, chunkContext) -> {
            System.out.println("lawdProvinceCode = " + lawdProvinceCode);
            return RepeatStatus.FINISHED;
        };
    }

    private CompositeJobParametersValidator aptDealJobParameterValidator() {
        return new CompositeJobParametersValidator() {{
            setValidators(
                List.of(new YearMonthParameterValidator(), new LawdCodeParameterValidator())
            );
        }};
    }

}
