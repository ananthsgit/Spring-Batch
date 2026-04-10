package com.springbatch.conditional.config;

import com.springbatch.conditional.decider.FileValidationDecider;
import com.springbatch.conditional.tasklet.GenerateReportTasklet;
import com.springbatch.conditional.tasklet.ProcessDataTasklet;
import com.springbatch.conditional.tasklet.SendNotificationTasklet;
import com.springbatch.conditional.tasklet.ValidateFileTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    Step validateStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, ValidateFileTasklet validateFileTasklet) {
        return new StepBuilder("validateStep", jobRepository)
                .tasklet(validateFileTasklet, transactionManager)
                .build();
    }

    @Bean
    Step processStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, ProcessDataTasklet processDataTasklet) {
        return new StepBuilder("processStep", jobRepository)
                .tasklet(processDataTasklet, transactionManager)
                .build();
    }

    @Bean
    Step reportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, GenerateReportTasklet generateReportTasklet) {
        return new StepBuilder("reportStep", jobRepository)
                .tasklet(generateReportTasklet, transactionManager)
                .build();
    }

    @Bean
    Step notificationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, SendNotificationTasklet sendNotificationTasklet) {
        return new StepBuilder("notificationStep", jobRepository)
                .tasklet(sendNotificationTasklet, transactionManager)
                .build();
    }

    @Bean
    Job conditionalJob(JobRepository jobRepository, FileValidationDecider fileValidationDecider, 
                       Step validateStep, Step processStep, Step reportStep, Step notificationStep) {
        return new JobBuilder("conditionalJob", jobRepository)
                .start(validateStep)
                .next(fileValidationDecider)
                .on("VALID").to(processStep)
                .from(fileValidationDecider)
                .on("INVALID").to(notificationStep)
                .from(processStep)
                .next(reportStep)
                .end()
                .build();
    }
}
