package com.example.house.batch.job.notification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.example.house.batch.adapter.ApartmentApiResource;
import com.example.house.batch.config.HouseBatchTestConfig;
import com.example.house.batch.domain.AptNotification;
import com.example.house.batch.domain.Lawd;
import com.example.house.batch.domain.repository.AptNotificationRepository;
import com.example.house.batch.domain.repository.LawdRepository;
import com.example.house.batch.dto.AptDto;
import com.example.house.batch.job.apt.AptDealInsertJobConfig;
import com.example.house.batch.service.AptService;
import com.example.house.batch.service.PrintNotificationService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = {AptNotificationJobConfig.class, HouseBatchTestConfig.class}
)
@SpringBatchTest
@ActiveProfiles("test")
class AptNotificationJobConfigTest {

    public static final String LAWD_PROVINCE_CODE = "11110";
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private AptNotificationRepository aptNotificationRepository;

    @MockBean
    private LawdRepository lawdRepository;

    @MockBean
    private AptService aptService;

    @MockBean
    private PrintNotificationService printNotificationService;


    @BeforeEach
    void setUp() {
        AptNotification notification = initNotification("dkstpgud@gmail.com", LAWD_PROVINCE_CODE, true);
        aptNotificationRepository.save(notification);
    }

    @AfterEach
    void tearDown() {
        aptNotificationRepository.deleteAll();
    }

    @Test
    void aptNotificationJobConfigTest() throws Exception {
        // given
        BDDMockito.given(lawdRepository.findByLawdCode(anyString()))
                  .willReturn(lawdFixture());
        BDDMockito.given(aptService.findAptInfo(anyString(), any()))
                  .willReturn(List.of(
                      new AptDto("레미안", 1000000L, 2016, 84.56),
                      new AptDto("힐스테이트", 1300000L, 2021, 98.56)
                  ));

        // when
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("requestDate", LocalDate.of(2021, 11, 1).toString())
            .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        BDDMockito.verify(printNotificationService, BDDMockito.times(1))
                  .send(anyString());
    }


    @Test
    void fail_requestDateIncorrect() throws Exception {
        // given
        BDDMockito.given(lawdRepository.findByLawdCode(anyString()))
                  .willReturn(lawdFixture());
        BDDMockito.given(aptService.findAptInfo(anyString(), any()))
                  .willReturn(List.of(
                      new AptDto("레미안", 1000000L, 2016, 84.56),
                      new AptDto("힐스테이트", 1300000L, 2021, 98.56)
                  ));

        // when
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("requestDate", "1234")
            .toJobParameters();

        Assertions.assertThatThrownBy(() -> jobLauncherTestUtils.launchJob(jobParameters))
                  .isInstanceOf(JobParametersInvalidException.class)
                  .hasMessageContaining("requestDate parameter is invalid");
        BDDMockito.verify(printNotificationService, Mockito.never())
                  .send(anyString());
    }


    @Test
    void success_ButNoResultToSend() throws Exception {
        // given
        BDDMockito.given(lawdRepository.findByLawdCode(anyString()))
                  .willReturn(lawdEmptyFixture());
        BDDMockito.given(aptService.findAptInfo(anyString(), any()))
                  .willReturn(Collections.emptyList());

        // when
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("requestDate", LocalDate.of(2021, 11, 2).toString())
            .toJobParameters();

        // then
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assertions.assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        BDDMockito.verify(printNotificationService, Mockito.never())
                  .send(anyString());
    }

    private Optional<Lawd> lawdEmptyFixture() {
        return Optional.empty();
    }

    private Optional<Lawd> lawdFixture() {
        return Optional.of(
            new Lawd() {{
                update(LAWD_PROVINCE_CODE, "서울특별시", true);
            }}
        );
    }

    private AptNotification initNotification(String email, String lawdProvinceCode, boolean enabled) {
        return AptNotification.builder()
                       .email(email)
                       .lawdProvinceCode(lawdProvinceCode)
                       .enabled(enabled)
                       .build();
    }

}