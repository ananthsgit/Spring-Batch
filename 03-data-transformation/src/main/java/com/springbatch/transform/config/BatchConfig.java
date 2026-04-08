package com.springbatch.transform.config;

import com.springbatch.transform.model.Employee;
import com.springbatch.transform.processor.EmployeeProcessor;
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

    @Bean
    FlatFileItemReader<Employee> reader() {
        return new FlatFileItemReaderBuilder<Employee>()
                .name("employeeItemReader")
                .resource(new ClassPathResource("data/employees.csv"))
                .delimited()
                .names("id", "firstName", "lastName", "email", "department", "salary")
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Employee.class);
                }})
                .build();
    }

    @Bean
    JdbcBatchItemWriter<Employee> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Employee>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO employee (id, first_name, last_name, email, department, salary, bonus) " +
                     "VALUES (:id, :firstName, :lastName, :email, :department, :salary, :bonus)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    Step transformEmployeeStep(JobRepository jobRepository,
                                      PlatformTransactionManager transactionManager,
                                      FlatFileItemReader<Employee> reader,
                                      EmployeeProcessor processor,
                                      JdbcBatchItemWriter<Employee> writer) {
        return new StepBuilder("transformEmployeeStep", jobRepository)
                .<Employee, Employee>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)  // Add processor for validation & transformation
                .writer(writer)
                .build();
    }

    @Bean
    Job transformEmployeeJob(JobRepository jobRepository, Step transformEmployeeStep) {
        return new JobBuilder("transformEmployeeJob", jobRepository)
                .start(transformEmployeeStep)
                .build();
    }
}
