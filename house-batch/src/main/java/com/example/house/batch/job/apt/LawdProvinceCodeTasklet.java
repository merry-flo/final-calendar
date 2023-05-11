package com.example.house.batch.job.apt;

import com.example.house.batch.domain.repository.LawdRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class LawdProvinceCodeTasklet implements Tasklet {

    public static final String LAWD_PROVINCE_CODE = "lawdProvinceCode";
    public static final String LAWD_PROVINCE_CODE_COUNT = "lawdProvinceCodeCount";
    public static final String LAWD_PROVINCE_CODE_LIST = "lawdProvinceCodeList";

    private final LawdRepository lawdRepository;

    private List<String> lawdProvinceCodeList;
    private int lawdProvinceCodeCount;
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext executionContext = getExecutionContext(chunkContext);
        initializeLawdProvinceCodeList(executionContext);
        lawdProvinceCodeCount = executionContext.getInt(LAWD_PROVINCE_CODE_COUNT);

        if (lawdProvinceCodeCount == 0) {
            contribution.setExitStatus(ExitStatus.COMPLETED);
            return RepeatStatus.FINISHED;
        }

        lawdProvinceCodeCount--;
        executionContext.putString(LAWD_PROVINCE_CODE, lawdProvinceCodeList.get(lawdProvinceCodeCount));
        executionContext.putInt(LAWD_PROVINCE_CODE_COUNT, lawdProvinceCodeCount);

        contribution.setExitStatus(new ExitStatus("CONTINUABLE"));
        return RepeatStatus.FINISHED;
    }

    private void initializeLawdProvinceCodeList(ExecutionContext executionContext) {
        if (executionContext.containsKey(LAWD_PROVINCE_CODE_LIST)) {
            lawdProvinceCodeList = (List<String>) executionContext.get("lawdProvinceCodeList");
        } else {
            lawdProvinceCodeList = lawdRepository.findAllDistinctLawdProvinceCode();
            executionContext.put(LAWD_PROVINCE_CODE_LIST, lawdProvinceCodeList);
            executionContext.putInt(LAWD_PROVINCE_CODE_COUNT, lawdProvinceCodeList.size());
        }
    }

    /*
        chunkContext.getStepContext().getJobExecutionContext()
        위의 방법으로 가지고온 executionContext 는 unmodifiableMap 이기 때문에 read only 이다.
     */
    private ExecutionContext getExecutionContext(ChunkContext chunkContext) {
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        return stepExecution.getJobExecution().getExecutionContext();
    }
}
