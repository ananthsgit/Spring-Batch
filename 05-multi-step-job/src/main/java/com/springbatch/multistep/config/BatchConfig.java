package com.springbatch.multistep.config;

import com.springbatch.multistep.model.Employee;
import com.springbatch.multistep.tasklet.CalculateStatsTasklet;
import com.springbatch.multistep.tasklet.GenerateReportTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    // ==================== STEP 1: Import CSV to Database ====================

    @Bean
    FlatFileItemReader<Employee> reader() {
        return new FlatFileItemReaderBuilder<Employee>()
                .name("employeeItemReader")
                .resource(new ClassPathResource("data/employees.csv"))
                .delimited()
                .names("id", "firstName", "lastName", "email", "department", "salary")
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {
                    {
                        setTargetType(Employee.class);
                    }
                })
                .build();
    }

    @Bean
    JdbcBatchItemWriter<Employee> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Employee>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO employee (id, first_name, last_name, email, department, salary) " +
                     "VALUES (:id, :firstName, :lastName, :email, :department, :salary)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    Step importEmployeeStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            FlatFileItemReader<Employee> reader,
                            JdbcBatchItemWriter<Employee> writer) {
        return new StepBuilder("importEmployeeStep", jobRepository)
                .<Employee, Employee>chunk(10, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    // ==================== STEP 2: Calculate Statistics ====================

    @Bean
    Step calculateStatsStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            CalculateStatsTasklet calculateStatsTasklet) {
        return new StepBuilder("calculateStatsStep", jobRepository)
                .tasklet(calculateStatsTasklet, transactionManager)
                .build();
    }

    // ==================== STEP 3: Generate Report ====================

    @Bean
    Step generateReportStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            GenerateReportTasklet generateReportTasklet) {
        return new StepBuilder("generateReportStep", jobRepository)
                .tasklet(generateReportTasklet, transactionManager)
                .build();
    }

    // ==================== JOB: Multi-Step Job ====================

    @Bean
    Job employeeProcessingJob(JobRepository jobRepository,
                              Step importEmployeeStep,
                              Step calculateStatsStep,
                              Step generateReportStep) {
        return new JobBuilder("employeeProcessingJob", jobRepository)
                .start(importEmployeeStep)
                .next(calculateStatsStep)
                .next(generateReportStep)
                .build();
    }
}
