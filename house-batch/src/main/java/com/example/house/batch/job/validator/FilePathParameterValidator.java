package com.example.house.batch.job.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

public class FilePathParameterValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String filePath = parameters.getString("filePath");
        if (!StringUtils.hasText(filePath)) {
            throw new JobParametersInvalidException("filePath parameter is missing");
        }

        Resource resource = new ClassPathResource(filePath);

        if (!resource.exists()) {
            throw new JobParametersInvalidException("file does not exist");
        }
    }
}
