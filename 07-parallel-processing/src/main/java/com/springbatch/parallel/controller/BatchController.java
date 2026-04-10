package com.springbatch.parallel.controller;

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
    private Job parallelImportJob;

    @PostMapping("/parallel-import")
    public String triggerParallelJob() {
        try {
            long startTime = System.currentTimeMillis();
            
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(parallelImportJob, params);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            return "✅ Parallel Import Job executed successfully in " + duration + "ms!";
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }
}
