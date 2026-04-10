package com.springbatch.conditional.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            System.out.println("========================================");
            System.out.println("⚠️  Sending error notification...");
            System.out.println("========================================");
            
            System.out.println("📧 Email sent to: admin@example.com");
            System.out.println("Subject: Batch Job Failed - File Validation Error");
            System.out.println("Message: The employee import job failed during file validation.");
            System.out.println("Action: Please check the CSV file and retry.");
            System.out.println("✅ Notification sent successfully!");
            
            return RepeatStatus.FINISHED;
        } catch (Exception e) {
            System.out.println("❌ Error sending notification: " + e.getMessage());
            return RepeatStatus.FINISHED;
        }
    }
}
