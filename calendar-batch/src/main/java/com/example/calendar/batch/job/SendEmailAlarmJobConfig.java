package com.example.calendar.batch.job;

import com.example.calendar.batch.dto.SendMailBatchReq;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SendEmailAlarmJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private static final int CHUNK_SIZE = 10;

    @Bean
    public Job sendEmailAlarmJob(
        Step sendScheduleAlarmStep,
        Step sendEngagementAlarmStep
    ) {
        return jobBuilderFactory.get("sendEmailAlarmJob")
                                .start(sendScheduleAlarmStep)
                                .next(sendEngagementAlarmStep)
                                .build();
    }

    @Bean
    public Step sendScheduleAlarmStep(
        ItemReader<SendMailBatchReq> sendScheduleAlarmReader,
        ItemWriter<SendMailBatchReq> sendAlarmWriter
    ) {
        return stepBuilderFactory.get("sendScheduleAlarmStep")
                                 .<SendMailBatchReq, SendMailBatchReq>chunk(CHUNK_SIZE)
                                 .reader(sendScheduleAlarmReader)
                                 .writer(sendAlarmWriter)
                                 .allowStartIfComplete(true)
                                 .build();
    }

    @Bean
    public Step sendEngagementAlarmStep(
        ItemReader<SendMailBatchReq> sendEngagementAlarmReader,
        ItemWriter<SendMailBatchReq> sendAlarmWriter
    ) {
        return stepBuilderFactory.get("sendEngagementAlarmStep")
                                 .<SendMailBatchReq, SendMailBatchReq>chunk(CHUNK_SIZE)
                                 .reader(sendEngagementAlarmReader)
                                 .writer(sendAlarmWriter)
                                 .allowStartIfComplete(true)
                                 .build();
    }

    @Bean
    public JdbcCursorItemReader<SendMailBatchReq> sendScheduleAlarmReader() {
        return new JdbcCursorItemReaderBuilder<SendMailBatchReq>()
            .dataSource(dataSource)
            .rowMapper(new BeanPropertyRowMapper<>(SendMailBatchReq.class))
            .sql(" select s.id, s.start_at, s.title, u.email\n"
                + " from schedules s\n"
                + "         join users u on u.id = s.writer_id\n"
                + " where s.start_at >= now() + interval 10 minute\n"
                + "  and s.start_at < now() + interval 11 minute;")
            .name("sendScheduleAlarmReader")
            .fetchSize(CHUNK_SIZE)
            .build();
    }

    @Bean
    public JdbcCursorItemReader<SendMailBatchReq> sendEngagementAlarmReader() {
        return new JdbcCursorItemReaderBuilder<SendMailBatchReq>()
            .dataSource(dataSource)
            .rowMapper(new BeanPropertyRowMapper<>(SendMailBatchReq.class))
            .sql(" select s.id, s.start_at, s.title, u.email\n"
                + " from engagements e\n"
                + "         join schedules s on e.schedule_id = s.id\n"
                + "         join users u on u.id = s.writer_id\n"
                + " where s.start_at >= now() + interval 10 minute\n"
                + "  and s.start_at < now() + interval 11 minute\n"
                + " and e.request_status = 'ACCEPTED';")
            .name("sendEngagementAlarmReader")
            .fetchSize(CHUNK_SIZE)
            .build();
    }

    @Bean
    public ItemWriter<SendMailBatchReq> sendAlarmWriter() {
        return items -> new RestTemplate().postForObject(
            "http://localhost:8080/api/batch/mail", items, Object.class
        );
    }


}
