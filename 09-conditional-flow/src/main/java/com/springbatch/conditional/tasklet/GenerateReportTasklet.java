package com.springbatch.conditional.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class GenerateReportTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            System.out.println("========================================");
            System.out.println("📋 Generating success report...");
            System.out.println("========================================");
            
            String report = "Employee Import Report\n" +
                    "Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" +
                    "Status: SUCCESS\n" +
                    "Records Processed: 15\n" +
                    "Records Inserted: 15\n" +
                    "Errors: 0\n" +
                    "Duration: ~2 seconds";
            
            System.out.println(report);
            System.out.println("✅ Report generated successfully!");
            
            return RepeatStatus.FINISHED;
        } catch (Exception e) {
            System.out.println("❌ Error generating report: " + e.getMessage());
            return RepeatStatus.FINISHED;
        }
    }
}
