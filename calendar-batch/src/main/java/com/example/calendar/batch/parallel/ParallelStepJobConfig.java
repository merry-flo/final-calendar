package com.example.calendar.batch.parallel;

import com.example.calendar.batch.dto.AirportInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

/*
    parallel step 은 단일 프로세스에서 task executor 를 통한 멀티 스레드를 활용해 step 단위에서 병렬 처리를 지원한다.
 */
@Configuration
@RequiredArgsConstructor
public class ParallelStepJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job parallelJob(Flow splitFlow) {
        return jobBuilderFactory.get("parallelJob")
                                .incrementer(new RunIdIncrementer())
                                .start(splitFlow)
                                .build()
                                .build();
    }

    @Bean
    public Flow splitFlow(
        TaskExecutor taskExecutor,
        Flow flowAirportInfoFileStep,
        Flow flowAnotherStep
    ) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
            .split(taskExecutor)
            .add(flowAirportInfoFileStep, flowAnotherStep)
            .build();
    }

    @Bean
    public Flow flowAirportInfoFileStep(Step airportInfoParallelStep) {
        return new FlowBuilder<SimpleFlow>("airportInfoParallelStep")
            .start(airportInfoParallelStep)
            .build();
    }

    @Bean
    public Flow flowAnotherStep(Step anotherStep) {
        return new FlowBuilder<SimpleFlow>("flowAnotherStep")
            .start(anotherStep)
            .build();
    }

    @Bean
    public Step airportInfoParallelStep (
        ItemReader<AirportInfoDto> multiThreadAirportInfoReader,
        ItemProcessor<AirportInfoDto, AirportInfoDto> multiThreadAirportInfoProcessor,
        ItemWriter<AirportInfoDto> multiThreadAirportInfoWriter
    ) {
        return stepBuilderFactory.get("airportInfoParallelStep")
                                 .<AirportInfoDto, AirportInfoDto>chunk(20)
                                 .reader(multiThreadAirportInfoReader)
                                 .processor(multiThreadAirportInfoProcessor)
                                 .writer(multiThreadAirportInfoWriter)
                                 .build();
    }


    @Bean
    public Step anotherStep() {
        return stepBuilderFactory.get("anotherStep")
                                 .tasklet((contribution, chunkContext) -> {
                                     Thread.sleep(500L);
                                     System.out.println("Another step completed. Thread name: " + Thread.currentThread().getName());
                                     return RepeatStatus.FINISHED;
                                 })
                                 .build();
    }

}
