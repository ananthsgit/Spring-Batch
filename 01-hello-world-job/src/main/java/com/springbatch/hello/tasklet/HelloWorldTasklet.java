package com.springbatch.hello.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        
        // Get job and step names from context
        String jobName = chunkContext.getStepContext().getJobName();
        String stepName = chunkContext.getStepContext().getStepName();
        
        // Print hello world message
        System.out.println("========================================");
        System.out.println("🎉 Hello from Spring Batch!");
        System.out.println("📦 Job Name: " + jobName);
        System.out.println("📌 Step Name: " + stepName);
        System.out.println("========================================");
        
        // Return FINISHED to indicate task completion
        return RepeatStatus.FINISHED;
    }
}
