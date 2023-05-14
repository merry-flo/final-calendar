package com.example.house.batch.job.lawd;

import static org.mockito.ArgumentMatchers.any;

import com.example.house.batch.config.HouseBatchTestConfig;
import com.example.house.batch.service.LawdService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(
    classes = {LawdInsertJobConfig.class, HouseBatchTestConfig.class})
@SpringBatchTest
@ActiveProfiles("test")
class LawdInsertJobConfigTestMock {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private LawdService lawdService;

    @Test
    void lawdInsertJobTest() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("filePath", "LAWD_CODE_SAMPLE.txt")
            .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        BDDMockito.verify(lawdService, BDDMockito.times(9)).upsert(any());
    }
}