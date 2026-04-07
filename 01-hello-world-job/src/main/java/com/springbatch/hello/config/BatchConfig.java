package com.springbatch.hello.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.springbatch.hello.tasklet.HelloWorldTasklet;

@Configuration
public class BatchConfig {

    @Bean
    Job helloWorldJob(JobRepository jobRepository, Step helloWorldStep) {
        return new JobBuilder("helloWorldJob", jobRepository)
                .start(helloWorldStep)
                .build();
    }

    @Bean
    Step helloWorldStep(JobRepository jobRepository, 
                               PlatformTransactionManager transactionManager,
                               HelloWorldTasklet tasklet) {
        return new StepBuilder("helloWorldStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }
}
