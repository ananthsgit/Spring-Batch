package com.springbatch.transform.controller;

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
    private Job transformEmployeeJob;

    @PostMapping("/transform")
    public String runTransformJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("startTime", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(transformEmployeeJob, params);
            return "✅ Data Transformation Job executed successfully! Check database for transformed records.";
        } catch (Exception e) {
            return "❌ Job failed: " + e.getMessage();
        }
    }
}
