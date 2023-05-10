package com.example.house.batch.job.apt;

import com.example.house.batch.adapter.ApartmentApiResource;
import com.example.house.batch.config.HouseBatchTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = {AptDealInsertJobConfig.class, HouseBatchTestConfig.class, ApartmentApiResource.class},
    properties = {"job.name=" + AptDealInsertJobConfig.JOB_NAME, "apartment.api.path=testPath", "apartment.api.serviceKey=testKey"}
)
@SpringBatchTest
@ActiveProfiles("test")
class AptDealInsertJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void aptDealInsertJobConfigTest() throws Exception {

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        Assertions.assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}