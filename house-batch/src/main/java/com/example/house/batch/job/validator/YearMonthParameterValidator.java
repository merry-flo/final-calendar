package com.example.house.batch.job.validator;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class YearMonthParameterValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String yearMonth = parameters.getString("yearMonth");
        if (yearMonth == null || yearMonth.length() != 7) {
            throw new JobParametersInvalidException("yearMonth parameter is missing or invalid");
        }

        try {
            YearMonth.parse(yearMonth);
        } catch (DateTimeParseException e) {
            throw new JobParametersInvalidException("yearMonth parameter is missing or invalid");
        }
    }
}
