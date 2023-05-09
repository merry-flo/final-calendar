package com.example.house.batch.job.lawd;

import com.example.house.batch.config.HouseBatchTestConfig;
import com.example.house.batch.config.JpaConfig;
import com.example.house.batch.domain.repository.LawdRepository;
import com.example.house.batch.service.LawdService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = {LawdInsertJobConfig.class, HouseBatchTestConfig.class, LawdService.class})
@SpringBatchTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class LawdInsertJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private LawdRepository lawdRepository;

    @AfterEach
    void tearDown() {
        lawdRepository.deleteAll();
    }

    @Test
    void lawdInsertJobTest() throws Exception {
        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        Assertions.assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        Assertions.assertThat(lawdRepository.count()).isEqualTo(9);
    }
}