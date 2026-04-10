# 🚀 Spring Batch Projects - Quick Reference Guide

---

## 📋 Project Overview

| # | Project | Difficulty | Port | Key Focus | Records |
|---|---------|-----------|------|-----------|---------|
| 01 | Hello World Job | ⭐ | 8080 | Basic structure | - |
| 02 | CSV to Database | ⭐⭐ | 8081 | Chunk processing | 15 |
| 03 | Data Transformation | ⭐⭐ | 8082 | Validation & transform | 15 |
| 04 | Database to CSV | ⭐⭐⭐ | 8083 | Reverse ETL | 15 |
| 05 | Multi-Step Job | ⭐⭐⭐ | 8084 | Job orchestration | 15 |
| 06 | Scheduled Batch | ⭐⭐⭐ | 8085 | Cron scheduling | 15 |
| 07 | Parallel Processing | ⭐⭐⭐⭐ | 8086 | Multi-threading | 100 |
| 08 | REST API Batch | ⭐⭐⭐⭐ | 8087 | REST integration | 15 |
| 09 | Conditional Flow | ⭐⭐⭐⭐ | 8088 | Decision logic | 15 |
| 10 | Complete ETL | ⭐⭐⭐⭐⭐ | 8089 | Production-ready | 15 |

---

## 🎯 Quick Start Commands

### Project 01: Hello World Job
```bash
cd 01-hello-world-job
mvn clean install
mvn spring-boot:run
# Trigger: POST http://localhost:8080/api/batch/hello
```

### Project 02: CSV to Database
```bash
cd 02-csv-to-database
mvn clean install
mvn spring-boot:run
# Trigger: POST http://localhost:8081/api/batch/import-csv
```

### Project 03: Data Transformation
```bash
cd 03-data-transformation
mvn clean install
mvn spring-boot:run
# Trigger: POST http://localhost:8082/api/batch/transform
```

### Project 04: Database to CSV
```bash
cd 04-database-to-csv
mvn clean install
mvn spring-boot:run
# Trigger: POST http://localhost:8083/api/batch/export-csv
```

### Project 05: Multi-Step Job
```bash
cd 05-multi-step-job
mvn clean install
mvn spring-boot:run
# Trigger: POST http://localhost:8084/api/batch/process-employees
```

### Project 06: Scheduled Batch
```bash
cd 06-scheduled-batch
mvn clean install
mvn spring-boot:run
# Trigger: POST http://localhost:8085/api/batch/trigger-now
# Auto: Daily at midnight (configurable)
```

### Project 07: Parallel Processing
```bash
cd 07-parallel-processing
mvn clean install
mvn spring-boot:run
# Trigger: POST http://localhost:8086/api/batch/parallel-import
```

### Project 08: REST API Batch
```bash
cd 08-rest-api-batch
mvn clean install
mvn spring-boot:run
# Trigger: POST http://localhost:8087/api/jobs/trigger
# Status: GET http://localhost:8087/api/jobs/status/{id}
# History: GET http://localhost:8087/api/jobs/history
```

### Project 09: Conditional Flow
```bash
cd 09-conditional-flow
mvn clean install
mvn spring-boot:run
# Trigger: POST http://localhost:8088/api/batch/conditional-flow
```

### Project 10: Complete ETL Pipeline
```bash
cd 10-complete-etl-pipeline
mvn clean install
mvn spring-boot:run
# Trigger: POST http://localhost:8089/api/batch/etl-pipeline
# Health: GET http://localhost:8089/actuator/health
```

---

## 🗄️ Database Setup (All Projects)

```sql
CREATE DATABASE spring_batch_db;

USE spring_batch_db;

CREATE TABLE employee (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    department VARCHAR(50),
    salary DECIMAL(10,2)
);
```

---

## 📊 Verification Queries

### Check Records Inserted
```sql
SELECT COUNT(*) as total_employees FROM employee;
```

### View Sample Data
```sql
SELECT id, first_name, last_name, email, department, salary 
FROM employee 
LIMIT 5;
```

### Check Job Execution
```sql
SELECT JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, EXIT_CODE
FROM BATCH_JOB_EXECUTION
ORDER BY JOB_EXECUTION_ID DESC
LIMIT 1;
```

### Check Step Statistics
```sql
SELECT STEP_NAME, READ_COUNT, WRITE_COUNT, SKIP_COUNT, COMMIT_COUNT, STATUS
FROM BATCH_STEP_EXECUTION
WHERE JOB_EXECUTION_ID = (SELECT MAX(JOB_EXECUTION_ID) FROM BATCH_JOB_EXECUTION);
```

### Clear Data Between Tests
```sql
TRUNCATE TABLE employee;
```

---

## 🔧 Configuration Template

All projects use similar `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/spring_batch_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Connection Pool
spring.datasource.hikari.maximum-pool-size=15
spring.datasource.hikari.minimum-idle=3

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Spring Batch Configuration
spring.batch.job.enabled=false
spring.batch.jdbc.initialize-schema=always

# Server Configuration
server.port=8080  # Change per project

# Project-Specific (if applicable)
batch.schedule.cron=0 0 0 * * ?  # Project 06
batch.thread.pool.size=10         # Project 07
management.endpoints.web.exposure.include=health,metrics  # Project 10
```

---

## 🧪 Testing with PowerShell

```powershell
# Project 01
Invoke-WebRequest -Uri http://localhost:8080/api/batch/hello -Method POST

# Project 02
Invoke-WebRequest -Uri http://localhost:8081/api/batch/import-csv -Method POST

# Project 03
Invoke-WebRequest -Uri http://localhost:8082/api/batch/transform -Method POST

# Project 04
Invoke-WebRequest -Uri http://localhost:8083/api/batch/export-csv -Method POST

# Project 05
Invoke-WebRequest -Uri http://localhost:8084/api/batch/process-employees -Method POST

# Project 06
Invoke-WebRequest -Uri http://localhost:8085/api/batch/trigger-now -Method POST

# Project 07
Invoke-WebRequest -Uri http://localhost:8086/api/batch/parallel-import -Method POST

# Project 08
Invoke-WebRequest -Uri http://localhost:8087/api/jobs/trigger -Method POST

# Project 09
Invoke-WebRequest -Uri http://localhost:8088/api/batch/conditional-flow -Method POST

# Project 10
Invoke-WebRequest -Uri http://localhost:8089/api/batch/etl-pipeline -Method POST
```

---

## 🧪 Testing with curl

```bash
# Project 01
curl -X POST http://localhost:8080/api/batch/hello

# Project 02
curl -X POST http://localhost:8081/api/batch/import-csv

# Project 03
curl -X POST http://localhost:8082/api/batch/transform

# Project 04
curl -X POST http://localhost:8083/api/batch/export-csv

# Project 05
curl -X POST http://localhost:8084/api/batch/process-employees

# Project 06
curl -X POST http://localhost:8085/api/batch/trigger-now

# Project 07
curl -X POST http://localhost:8086/api/batch/parallel-import

# Project 08
curl -X POST http://localhost:8087/api/jobs/trigger
curl -X GET http://localhost:8087/api/jobs/history

# Project 09
curl -X POST http://localhost:8088/api/batch/conditional-flow

# Project 10
curl -X POST http://localhost:8089/api/batch/etl-pipeline
curl -X GET http://localhost:8089/actuator/health
```

---

## 📚 Key Concepts by Project

| Project | Main Concepts |
|---------|---------------|
| 01 | Job, Step, Tasklet, JobLauncher, JobRepository |
| 02 | ItemReader, ItemWriter, Chunk Processing, FlatFileItemReader |
| 03 | ItemProcessor, Validation, Skip Logic, Data Transformation |
| 04 | JdbcCursorItemReader, FlatFileItemWriter, RowMapper |
| 05 | Multi-Step Job, ExecutionContext, Job Orchestration |
| 06 | @Scheduled, Cron Expressions, @EnableScheduling |
| 07 | TaskExecutor, Multi-Threading, Thread Pool, Performance |
| 08 | REST Integration, JobLauncher, JobExplorer, DTOs |
| 09 | JobExecutionDecider, Conditional Flow, Exit Status |
| 10 | ETL, Production Patterns, Monitoring, Error Handling |

---

## 🎓 Learning Progression

```
Beginner (Project 01)
    ↓
Basic Processing (Projects 02-04)
    ↓
Advanced Patterns (Projects 05-06)
    ↓
Performance & Integration (Projects 07-08)
    ↓
Complex Workflows (Project 09)
    ↓
Production Ready (Project 10)
```

---

## 🐛 Common Issues & Solutions

### Issue: Port Already in Use
```bash
# Change port in application.properties
server.port=8090
```

### Issue: Database Connection Error
```bash
# Verify MySQL is running and credentials are correct
# Check application.properties database URL and credentials
```

### Issue: Duplicate Key Error
```sql
-- Clear data before re-running
TRUNCATE TABLE employee;
```

### Issue: CSV File Not Found
```bash
# Ensure employees.csv is in src/main/resources/data/
```

### Issue: Job Runs Automatically
```properties
# Disable auto-run in application.properties
spring.batch.job.enabled=false
```

---

## 📈 Performance Expectations

| Project | Records | Time | Throughput |
|---------|---------|------|-----------|
| 01 | - | <1s | - |
| 02 | 15 | 2-3s | 5 rec/s |
| 03 | 15 | 2-3s | 5 rec/s |
| 04 | 15 | 2-3s | 5 rec/s |
| 05 | 15 | 5s | 3 rec/s |
| 06 | 15 | 2-3s | 5 rec/s |
| 07 | 100 | 1-3s | 33-100 rec/s |
| 08 | 15 | 2-3s | 5 rec/s |
| 09 | 15 | 2s | 7.5 rec/s |
| 10 | 15 | 2-3s | 5-7 rec/s |

---

## 🔗 Project Dependencies

```
Project 01 (standalone)
    ↓
Project 02 (uses Project 01 concepts)
    ↓
Project 03 (uses Project 02 concepts)
    ↓
Project 04 (uses Project 02 data)
    ↓
Project 05 (uses Projects 02-04 concepts)
    ↓
Project 06 (uses Project 02 concepts)
    ↓
Project 07 (uses Project 02 concepts)
    ↓
Project 08 (uses Project 02 concepts)
    ↓
Project 09 (uses Projects 02-05 concepts)
    ↓
Project 10 (uses all previous concepts)
```

---

## 📖 Documentation Files

- `README.md` - Main documentation for each project
- `pom.xml` - Maven dependencies
- `application.properties` - Configuration
- `BatchConfig.java` - Job configuration
- `BatchController.java` - REST endpoints
- `Employee.java` - JPA entity
- `employees.csv` - Sample data

---

## ✅ Pre-Execution Checklist

Before running any project:

- [ ] Java 17+ installed
- [ ] MySQL 8.x running
- [ ] Maven 3.6+ installed
- [ ] Database created: `spring_batch_db`
- [ ] Employee table created
- [ ] `application.properties` updated with credentials
- [ ] Port not in use
- [ ] CSV file in correct location (if applicable)
- [ ] IDE or terminal ready

---

## 🚀 Deployment Checklist

Before deploying to production:

- [ ] All tests passing
- [ ] Error handling implemented
- [ ] Logging configured
- [ ] Monitoring enabled
- [ ] Performance optimized
- [ ] Database backups configured
- [ ] Security credentials secured
- [ ] Documentation updated
- [ ] Health checks verified
- [ ] Metrics exposed

---

## 📞 Support Resources

- **Spring Batch Documentation:** https://spring.io/projects/spring-batch
- **Spring Boot Documentation:** https://spring.io/projects/spring-boot
- **MySQL Documentation:** https://dev.mysql.com/doc/
- **Maven Documentation:** https://maven.apache.org/

---

## 🎓 Next Steps

1. Start with **Project 01** to understand basics
2. Progress through projects in order
3. Complete all setup instructions
4. Test each project thoroughly
5. Review troubleshooting section if issues arise
6. Explore advanced patterns in later projects
7. Apply concepts to your own batch jobs

---

**Last Updated:** April 2026  
**Status:** ✅ All Projects Ready  
**Quality:** Production-Ready
