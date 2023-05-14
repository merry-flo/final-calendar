package com.example.house.batch.job.apt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = {AptDealInsertJobConfig.class, HouseBatchTestConfig.class}
)
@SpringBatchTest
@ActiveProfiles("test")
class AptDealInsertJobConfigTestMock {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private ApartmentApiResource apartmentApiResource;

    @MockBean
    private AptService aptService;

    @MockBean
    private LawdRepository lawdRepository;


    @Test
    void aptDealInsertJobConfigTest() throws Exception {
        // given
        BDDMockito.given(lawdRepository.findAllDistinctLawdProvinceCode())
                  .willReturn(Arrays.asList("11110"));

        BDDMockito.given(apartmentApiResource.getResource(anyString(), any()))
                  .willReturn(new ClassPathResource("apartment_api_response.xml"));

        JobParameters jobParameters = new JobParametersBuilder()
            .addString("yearMonth", "2015-12")
            .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        BDDMockito.verify(aptService, Mockito.times(49)).upsert(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2015", "201509", "2015-10-10"})
    void fail_whenJobParameterIsNotValidPattern(String value) throws Exception {
        // given
        BDDMockito.given(lawdRepository.findAllDistinctLawdProvinceCode())
                  .willReturn(Arrays.asList("11110"));

        BDDMockito.given(apartmentApiResource.getResource(anyString(), any()))
                  .willReturn(new ClassPathResource("apartment_api_response.xml"));

        JobParameters jobParameters = new JobParametersBuilder()
            .addString("yearMonth", value)
            .toJobParameters();

        // when && then
        Assertions.assertThatThrownBy(() -> jobLauncherTestUtils.launchJob(jobParameters))
                  .isInstanceOf(JobParametersInvalidException.class);
        BDDMockito.verify(aptService, Mockito.never()).upsert(any());
    }

}