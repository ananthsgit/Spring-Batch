package com.springbatch.conditional.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

@Component
public class FileValidationDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (stepExecution == null) {
            return new FlowExecutionStatus("VALID");
        }

        String validationStatus = stepExecution.getExecutionContext().getString("validationStatus");
        
        if ("VALID".equals(validationStatus)) {
            return new FlowExecutionStatus("VALID");
        } else {
            return new FlowExecutionStatus("INVALID");
        }
    }
}
