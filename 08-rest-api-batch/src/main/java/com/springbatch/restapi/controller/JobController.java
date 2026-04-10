package com.springbatch.restapi.controller;

import com.springbatch.restapi.dto.JobLaunchRequest;
import com.springbatch.restapi.dto.JobStatusResponse;
import com.springbatch.restapi.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private BatchService batchService;

    @PostMapping("/start")
    public ResponseEntity<JobStatusResponse> startJob(@RequestBody JobLaunchRequest request) {
        JobStatusResponse response = batchService.startJob(request.getJobName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{executionId}")
    public ResponseEntity<JobStatusResponse> getJobStatus(@PathVariable Long executionId) {
        JobStatusResponse response = batchService.getJobStatus(executionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<JobStatusResponse>> getJobHistory() {
        List<JobStatusResponse> responses = batchService.getAllJobExecutions();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/trigger")
    public ResponseEntity<JobStatusResponse> triggerJob() {
        JobStatusResponse response = batchService.startJob("importEmployeeJob");
        return ResponseEntity.ok(response);
    }
}
