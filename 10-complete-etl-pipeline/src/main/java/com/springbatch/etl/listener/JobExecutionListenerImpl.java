package com.springbatch.etl.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JobExecutionListenerImpl implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("========================================");
        System.out.println("🚀 Job Started: " + jobExecution.getJobInstance().getJobName());
        System.out.println("Start Time: " + jobExecution.getStartTime());
        System.out.println("========================================");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long duration = 0;
        if (jobExecution.getStartTime() != null && jobExecution.getEndTime() != null) {
            duration = Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime()).toMillis();
        }

        System.out.println("========================================");
        System.out.println("✅ Job Completed: " + jobExecution.getJobInstance().getJobName());
        System.out.println("Status: " + jobExecution.getStatus());
        System.out.println("End Time: " + jobExecution.getEndTime());
        System.out.println("Duration: " + duration + "ms");
        System.out.println("========================================");
    }
}
