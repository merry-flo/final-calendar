package com.example.house.batch.job.apt;

import com.example.house.batch.adapter.ApartmentApiResource;
import com.example.house.batch.config.HouseBatchTestConfig;
import com.example.house.batch.domain.Apt;
import com.example.house.batch.domain.AptDeal;
import com.example.house.batch.domain.Lawd;
import com.example.house.batch.domain.repository.AptDealRepository;
import com.example.house.batch.domain.repository.AptRepository;
import com.example.house.batch.domain.repository.LawdRepository;
import com.example.house.batch.service.AptService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@SpringBootTest(
    classes = {AptDealInsertJobConfig.class, HouseBatchTestConfig.class, ApartmentApiResource.class, AptService.class},
    properties = {"job.name=" + AptDealInsertJobConfig.JOB_NAME, "apartment.api.path=testPath",
        "apartment.api.serviceKey=testKey"}
)
@SpringBatchTest
@ActiveProfiles("test")
class AptDealInsertJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private LawdRepository lawdRepository;

    @Autowired
    private AptRepository aptRepository;

    @Autowired
    private AptDealRepository aptDealRepository;

    @BeforeEach
    public void setUp() throws IOException {
        File sampleFile = new ClassPathResource("LAWD_CODE_SAMPLE.txt").getFile();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sampleFile)));

        String line = null;
        while((line = reader.readLine()) != null) {
            String[] split = line.split("\t");
            lawdRepository.save(
                new Lawd(split[0], split[1], "존재".equals(split[2]))
            );
        }
        reader.close();
    }

    @Test
    void aptDealInsertJobConfigTest() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("yearMonth", "202106")
            .addLong("version", 1L)
            .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        Assertions.assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        List<Apt> allApt = aptRepository.findAll();
        List<AptDeal> allAptDeal = aptDealRepository.findAll();

        Assertions.assertThat(allApt).isNotEmpty();
        Assertions.assertThat(allApt.size()).isLessThanOrEqualTo(allAptDeal.size());
    }
}