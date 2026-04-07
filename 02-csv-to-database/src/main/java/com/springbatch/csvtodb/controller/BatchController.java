package com.springbatch.csvtodb.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importEmployeeJob;

    @PostMapping("/import-csv")
    public String importCsvToDatabase() {
        try {
            // Create unique job parameters
            JobParameters params = new JobParametersBuilder()
                    .addLong("startTime", System.currentTimeMillis())
                    .toJobParameters();
            
            // Launch the job
            jobLauncher.run(importEmployeeJob, params);
            
            return "✅ CSV Import Job executed successfully! Check database for records.";
        } catch (Exception e) {
            return "❌ Job failed: " + e.getMessage();
        }
    }
}
