package com.springbatch.parallel.config;

import com.springbatch.parallel.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Bean
    TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("batch-");
        executor.initialize();
        return executor;
    }

    @Bean
    FlatFileItemReader<Employee> reader() {
        return new FlatFileItemReaderBuilder<Employee>()
                .name("employeeReader")
                .resource(new ClassPathResource("data/employees.csv"))
                .delimited()
                .names("id", "firstName", "lastName", "email", "department", "salary")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
                    setTargetType(Employee.class);
                }})
                .linesToSkip(1)
                .build();
    }

    @Bean
    JdbcBatchItemWriter<Employee> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Employee>()
                .itemSqlParameterSourceProvider(BeanPropertySqlParameterSource::new)
                .sql("INSERT INTO employee (id, first_name, last_name, email, department, salary) VALUES (:id, :firstName, :lastName, :email, :department, :salary)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    Step parallelImportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource, TaskExecutor taskExecutor) {
        return new StepBuilder("parallelImportStep", jobRepository)
                .<Employee, Employee>chunk(10, transactionManager)
                .reader(reader())
                .writer(writer(dataSource))
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    Job parallelImportJob(JobRepository jobRepository, Step parallelImportStep) {
        return new JobBuilder("parallelImportJob", jobRepository)
                .start(parallelImportStep)
                .build();
    }
}
