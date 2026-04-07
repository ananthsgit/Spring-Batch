package com.springbatch.csvtodb.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.springbatch.csvtodb.model.Employee;

@Configuration
public class BatchConfig {

    // ========================================
    // READER: Read from CSV file
    // ========================================
    @Bean
    FlatFileItemReader<Employee> reader() {
        FlatFileItemReader<Employee> reader = new FlatFileItemReader<>();
        
        // Set CSV file location
        reader.setResource(new ClassPathResource("data/employees.csv"));
        
        // Skip header line
        reader.setLinesToSkip(1);
        
        // Configure line mapper
        reader.setLineMapper(lineMapper());
        
        return reader;
    }

    @Bean
    DefaultLineMapper<Employee> lineMapper() {
        DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();
        
        // Tokenizer: Split CSV line by comma
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id", "firstName", "lastName", "email", "department", "salary");
        
        // Field Set Mapper: Map tokens to Employee object
        BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Employee.class);
        
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        
        return lineMapper;
    }

    // ========================================
    // WRITER: Write to MySQL database
    // ========================================
    @Bean
    JdbcBatchItemWriter<Employee> writer(DataSource dataSource) {
        JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<>();
        
        writer.setDataSource(dataSource);
        
        // SQL insert statement
        writer.setSql("INSERT INTO employee (id, first_name, last_name, email, department, salary) " +
                      "VALUES (:id, :firstName, :lastName, :email, :department, :salary)");
        
        // Map Employee properties to SQL parameters
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        
        return writer;
    }

    // ========================================
    // STEP: Read → Write (Chunk size: 10)
    // ========================================
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

    // ========================================
    // JOB: Import CSV to Database
    // ========================================
    @Bean
    Job importEmployeeJob(JobRepository jobRepository, Step importEmployeeStep) {
        return new JobBuilder("importEmployeeJob", jobRepository)
                .start(importEmployeeStep)
                .build();
    }
}
