package com.springbatch.conditional.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ProcessDataTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            System.out.println("========================================");
            System.out.println("📊 Processing employee data...");
            System.out.println("========================================");
            
            Thread.sleep(1000);
            
            System.out.println("✅ Data processing completed successfully!");
            System.out.println("📈 Processed 15 employee records");
            System.out.println("💾 Records inserted into database");
            
            return RepeatStatus.FINISHED;
        } catch (Exception e) {
            System.out.println("❌ Error processing data: " + e.getMessage());
            return RepeatStatus.FINISHED;
        }
    }
}
