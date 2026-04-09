package com.springbatch.multistep;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class MultiStepApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiStepApplication.class, args);
    }
}
