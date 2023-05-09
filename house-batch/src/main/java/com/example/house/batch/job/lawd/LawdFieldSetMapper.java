package com.example.house.batch.job.lawd;

import com.example.house.batch.domain.Lawd;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class LawdFieldSetMapper implements FieldSetMapper<Lawd> {

    public static final String LAWD_CODE = "lawdCode";
    public static final String LAWD_PROVINCE = "lawdProvince";
    public static final String EXIST = "exist";
    private static final String TRUE_VALUE = "존재";

    @Override
    public Lawd mapFieldSet(FieldSet fieldSet) throws BindException {
        return new Lawd(
            fieldSet.readString(LAWD_CODE),
            fieldSet.readString(LAWD_PROVINCE),
            fieldSet.readBoolean(EXIST, TRUE_VALUE)
        );
    }
}
