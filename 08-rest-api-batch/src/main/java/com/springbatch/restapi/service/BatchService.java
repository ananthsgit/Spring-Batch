package com.springbatch.restapi.service;

import com.springbatch.restapi.dto.JobStatusResponse;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatchService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importEmployeeJob;

    @Autowired
    private JobExplorer jobExplorer;

    public JobStatusResponse startJob(String jobName) {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(importEmployeeJob, params);

            return new JobStatusResponse(
                    execution.getId(),
                    execution.getJobInstance().getJobName(),
                    execution.getStatus().toString(),
                    execution.getStartTime(),
                    execution.getEndTime(),
                    execution.getExitStatus().getExitCode(),
                    "✅ Job started successfully!"
            );
        } catch (Exception e) {
            return new JobStatusResponse(
                    null,
                    jobName,
                    "FAILED",
                    null,
                    null,
                    "ERROR",
                    "❌ Error: " + e.getMessage()
            );
        }
    }

    public JobStatusResponse getJobStatus(Long executionId) {
        try {
            JobExecution execution = jobExplorer.getJobExecution(executionId);
            
            if (execution == null) {
                return new JobStatusResponse(
                        executionId,
                        "UNKNOWN",
                        "NOT_FOUND",
                        null,
                        null,
                        "ERROR",
                        "❌ Job execution not found!"
                );
            }

            return new JobStatusResponse(
                    execution.getId(),
                    execution.getJobInstance().getJobName(),
                    execution.getStatus().toString(),
                    execution.getStartTime(),
                    execution.getEndTime(),
                    execution.getExitStatus().getExitCode(),
                    "✅ Job status retrieved successfully!"
            );
        } catch (Exception e) {
            return new JobStatusResponse(
                    executionId,
                    "UNKNOWN",
                    "ERROR",
                    null,
                    null,
                    "ERROR",
                    "❌ Error: " + e.getMessage()
            );
        }
    }

    public List<JobStatusResponse> getAllJobExecutions() {
        List<JobStatusResponse> responses = new ArrayList<>();
        try {
            jobExplorer.getJobInstances("importEmployeeJob", 0, 10).forEach(instance -> {
                jobExplorer.getJobExecutions(instance).forEach(execution -> {
                    responses.add(new JobStatusResponse(
                            execution.getId(),
                            execution.getJobInstance().getJobName(),
                            execution.getStatus().toString(),
                            execution.getStartTime(),
                            execution.getEndTime(),
                            execution.getExitStatus().getExitCode(),
                            "Job execution record"
                    ));
                });
            });
        } catch (Exception e) {
            JobStatusResponse error = new JobStatusResponse();
            error.setMessage("❌ Error: " + e.getMessage());
            responses.add(error);
        }
        return responses;
    }
}
