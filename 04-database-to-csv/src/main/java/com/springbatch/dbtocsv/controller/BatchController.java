package com.springbatch.dbtocsv.controller;

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
    private Job exportEmployeeJob;

    @PostMapping("/export-csv")
    public String runExportJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("startTime", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(exportEmployeeJob, params);
            return "✅ Database to CSV Export Job executed successfully! Check output/employees_export.csv";
        } catch (Exception e) {
            return "❌ Job failed: " + e.getMessage();
        }
    }
}
