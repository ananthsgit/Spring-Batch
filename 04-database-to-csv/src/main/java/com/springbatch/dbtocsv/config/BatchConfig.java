package com.springbatch.dbtocsv.config;

import com.springbatch.dbtocsv.mapper.EmployeeRowMapper;
import com.springbatch.dbtocsv.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Bean
    JdbcCursorItemReader<Employee> reader(DataSource dataSource, EmployeeRowMapper rowMapper) {
        return new JdbcCursorItemReaderBuilder<Employee>()
                .name("employeeItemReader")
                .dataSource(dataSource)
                .sql("SELECT id, first_name, last_name, email, department, salary FROM employee")
                .rowMapper(rowMapper)
                .build();
    }

    @Bean
    FlatFileItemWriter<Employee> writer() {
        return new FlatFileItemWriterBuilder<Employee>()
                .name("employeeItemWriter")
                .resource(new FileSystemResource("src/main/resources/output/employees_export.csv"))
                .delimited()
                .delimiter(",")
                .names("id", "firstName", "lastName", "email", "department", "salary")
                .headerCallback(writer -> writer.write("id,firstName,lastName,email,department,salary"))
                .build();
    }

    @Bean
    Step exportEmployeeStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   JdbcCursorItemReader<Employee> reader,
                                   FlatFileItemWriter<Employee> writer) {
        return new StepBuilder("exportEmployeeStep", jobRepository)
                .<Employee, Employee>chunk(10, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    Job exportEmployeeJob(JobRepository jobRepository, Step exportEmployeeStep) {
        return new JobBuilder("exportEmployeeJob", jobRepository)
                .start(exportEmployeeStep)
                .build();
    }
}
