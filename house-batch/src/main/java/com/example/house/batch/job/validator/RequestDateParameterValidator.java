package com.example.house.batch.job.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class RequestDateParameterValidator implements JobParametersValidator {

    public static final String REQUEST_DATE = "requestDate";

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String requestDate = parameters.getString(REQUEST_DATE);

        if (!StringUtils.hasText(requestDate)) {
            throw new JobParametersInvalidException("requestDate parameter is missing");
        }

        try {
            LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new JobParametersInvalidException("requestDate parameter is invalid");
        }

    }
}
