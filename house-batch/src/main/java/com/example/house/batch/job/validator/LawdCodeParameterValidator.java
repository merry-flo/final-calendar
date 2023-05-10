package com.example.house.batch.job.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class LawdCodeParameterValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String lawdCode = parameters.getString("lawdCode");
        if (isNotValid(lawdCode)) {
            throw new JobParametersInvalidException("lawdCode는 5자리 숫자로 입력해주세요.");
        }

        try {
            Integer.parseInt(lawdCode);
        } catch (NumberFormatException e) {
            throw new JobParametersInvalidException("lawdCode는 5자리 숫자로 입력해주세요.");
        }
    }

    private boolean isNotValid(String lawdCode) {
        return lawdCode == null || lawdCode.length() != 5;
    }
}
