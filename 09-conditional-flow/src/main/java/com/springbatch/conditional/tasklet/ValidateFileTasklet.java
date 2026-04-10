package com.springbatch.conditional.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ValidateFileTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            ClassPathResource resource = new ClassPathResource("data/employees.csv");
            File file = resource.getFile();
            
            if (file.exists() && file.length() > 0) {
                System.out.println("✅ File validation successful: " + file.getAbsolutePath());
                contribution.getStepExecution().getExecutionContext().put("validationStatus", "VALID");
                return RepeatStatus.FINISHED;
            } else {
                System.out.println("❌ File validation failed: File not found or empty");
                contribution.getStepExecution().getExecutionContext().put("validationStatus", "INVALID");
                return RepeatStatus.FINISHED;
            }
        } catch (Exception e) {
            System.out.println("❌ File validation error: " + e.getMessage());
            contribution.getStepExecution().getExecutionContext().put("validationStatus", "INVALID");
            return RepeatStatus.FINISHED;
        }
    }
}
