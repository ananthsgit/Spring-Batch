package com.springbatch.restapi.dto;

public class JobLaunchRequest {
    private String jobName;

    public JobLaunchRequest() {
    }

    public JobLaunchRequest(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public String toString() {
        return "JobLaunchRequest{" +
                "jobName='" + jobName + '\'' +
                '}';
    }
}
