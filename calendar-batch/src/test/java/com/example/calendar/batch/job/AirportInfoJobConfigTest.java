package com.example.calendar.batch.job;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.batch.core.ExitStatus.*;

import com.example.calendar.batch.BatchTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {AirportInfoJobConfig.class, BatchTestConfig.class})
class AirportInfoJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void successAirportInfoJobConfigTest() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        Assertions.assertThat(jobExecution.getExitStatus()).isEqualTo(COMPLETED);
        AssertFile.assertFileEquals(
            new FileSystemResource("success_airport_info_ko.csv"),
            new FileSystemResource("airport_info_ko.csv")
        );
    }
}