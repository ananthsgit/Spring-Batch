package com.springbatch.etl.config;

import com.springbatch.etl.listener.JobExecutionListenerImpl;
import com.springbatch.etl.model.Employee;
import com.springbatch.etl.processor.EmployeeProcessor;
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
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("etl-");
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
    EmployeeProcessor processor() {
        return new EmployeeProcessor();
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
    Step etlStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource, TaskExecutor taskExecutor) {
        return new StepBuilder("etlStep", jobRepository)
                .<Employee, Employee>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer(dataSource))
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    Job etlPipelineJob(JobRepository jobRepository, Step etlStep, JobExecutionListenerImpl jobExecutionListener) {
        return new JobBuilder("etlPipelineJob", jobRepository)
                .listener(jobExecutionListener)
                .start(etlStep)
                .build();
    }
}
