package com.springbatch.hello.controller;

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
    private Job helloWorldJob;

    @PostMapping("/hello")
    public String runHelloWorldJob() {
        try {
            // Create unique job parameters using current timestamp
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            
            // Launch the job
            jobLauncher.run(helloWorldJob, params);
            
            return "✅ Hello World Job executed successfully!";
        } catch (Exception e) {
            return "❌ Job failed: " + e.getMessage();
        }
    }
}
