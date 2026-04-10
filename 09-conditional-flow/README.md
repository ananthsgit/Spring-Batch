# 📦 Project 09: Conditional Flow & Decision Logic

**Difficulty:** ⭐⭐⭐⭐ Expert

---

## 🎯 What This Project Does

A Spring Batch application that demonstrates dynamic job flows with decision logic. Uses `JobExecutionDecider` to make runtime decisions about which step to execute next based on conditions.

**Purpose:** Learn conditional step execution, decision logic, and complex workflow patterns

---

## 📚 Key Concepts Learned

| Concept | Description |
|---------|-------------|
| **JobExecutionDecider** | Makes runtime decisions about job flow |
| **Conditional Flow** | Execute different steps based on conditions |
| **ExecutionContext** | Share data between steps for decision making |
| **Flow Control** | on(), to(), from(), end() methods |
| **Exit Status** | Custom exit codes to control flow |
| **Error Handling** | Different paths for success and failure |

---

## 🏗️ Architecture

```
Job Start
    ↓
Step 1: Validate File
    ↓
Decider: File Valid?
    ├── YES → Step 2: Process Data
    │           ↓
    │       Step 3: Generate Report
    │           ↓
    │       SUCCESS
    │
    └── NO → Step 4: Send Error Notification
                ↓
            FAILED
```

---

## 📂 Project Structure

```
09-conditional-flow/
├── src/main/java/com/springbatch/conditional/
│   ├── ConditionalFlowApplication.java  # Main Spring Boot app
│   ├── config/
│   │   └── BatchConfig.java             # Job with conditional flow
│   ├── model/
│   │   └── Employee.java                # JPA Entity
│   ├── decider/
│   │   └── FileValidationDecider.java   # Decision logic
│   ├── tasklet/
│   │   ├── ValidateFileTasklet.java     # Validate file
│   │   ├── ProcessDataTasklet.java      # Process data
│   │   ├── GenerateReportTasklet.java   # Generate report
│   │   └── SendNotificationTasklet.java # Send notification
│   └── controller/
│       └── BatchController.java         # REST endpoint
├── src/main/resources/
│   ├── application.properties           # Database & server config
│   └── data/
│       └── employees.csv                # Sample CSV
├── pom.xml                              # Maven dependencies
└── README.md                            # This file
```

---

## 🔧 Technologies Used

| Technology | Version | Purpose |
|-----------|---------|---------|
| Spring Boot | 3.5.13 | Application framework |
| Spring Batch | 5.x | Batch processing |
| Spring Data JPA | Latest | ORM & database access |
| MySQL | 8.x | Database |
| Java | 17+ | Programming language |
| Maven | Latest | Build tool |

---

## 🔀 Flow Scenarios

### Scenario 1: Success Path (File Valid)
```
Validate → [VALID] → Process → Generate Report → COMPLETED
```

### Scenario 2: Validation Failure (File Invalid)
```
Validate → [INVALID] → Send Notification → FAILED
```

---

## ⚙️ Setup Instructions

### Prerequisites
- Java 17 or higher
- MySQL 8.x
- Maven 3.6+
- Spring Tool Suite or any IDE

### Step 1: Database Setup

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

### Step 2: Update Configuration

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/spring_batch_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Spring Batch Configuration
spring.batch.job.enabled=false
spring.batch.jdbc.initialize-schema=always

# Server Configuration
server.port=8088
```

### Step 3: Build & Run

**Option A: Using Maven**
```bash
cd 09-conditional-flow
mvn clean install
mvn spring-boot:run
```

**Option B: Using Spring Tool Suite**
1. Right-click `ConditionalFlowApplication.java`
2. Select **Run As** → **Spring Boot App**
3. Wait for: `Started ConditionalFlowApplication in X seconds`

---

## 🧪 Testing & Triggering the Job

### Trigger the Conditional Flow Job

**Using PowerShell (Windows):**
```powershell
Invoke-WebRequest -Uri http://localhost:8088/api/batch/conditional-flow -Method POST
```

**Using curl (Linux/Mac):**
```bash
curl -X POST http://localhost:8088/api/batch/conditional-flow
```

**Using Postman:**
- Method: `POST`
- URL: `http://localhost:8088/api/batch/conditional-flow`
- Click Send

**Expected Response:**
```
✅ Conditional Flow Job triggered successfully!
```

---

## 📊 Checking Output

### 1. Console Output - Success Path

When file is valid:
```
========================================
✅ File validation successful: /path/to/employees.csv
========================================
📊 Processing employee data...
========================================
✅ Data processing completed successfully!
📈 Processed 15 employee records
💾 Records inserted into database
========================================
📋 Generating success report...
========================================
Employee Import Report
Date: 2026-04-10 10:30:00
Status: SUCCESS
Records Processed: 15
Records Inserted: 15
Errors: 0
Duration: ~2 seconds
✅ Report generated successfully!
```

### 2. Console Output - Failure Path

When file is invalid (rename/delete employees.csv):
```
========================================
❌ File validation failed: File not found or empty
========================================
⚠️  Sending error notification...
========================================
📧 Email sent to: admin@example.com
Subject: Batch Job Failed - File Validation Error
Message: The employee import job failed during file validation.
Action: Please check the CSV file and retry.
✅ Notification sent successfully!
```

### 3. Check Job Execution History

```sql
SELECT JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, EXIT_CODE
FROM BATCH_JOB_EXECUTION
ORDER BY JOB_EXECUTION_ID DESC
LIMIT 1;

-- Expected output:
-- JOB_EXECUTION_ID | START_TIME | END_TIME | STATUS | EXIT_CODE
-- X | 2026-04-10 10:30:... | 2026-04-10 10:30:... | COMPLETED | COMPLETED
```

### 4. Check Step Execution

```sql
SELECT STEP_NAME, STATUS
FROM BATCH_STEP_EXECUTION
WHERE JOB_EXECUTION_ID = (SELECT MAX(JOB_EXECUTION_ID) FROM BATCH_JOB_EXECUTION)
ORDER BY STEP_EXECUTION_ID;

-- Expected output (Success Path):
-- STEP_NAME | STATUS
-- validateStep | COMPLETED
-- processStep | COMPLETED
-- reportStep | COMPLETED

-- Expected output (Failure Path):
-- STEP_NAME | STATUS
-- validateStep | COMPLETED
-- notificationStep | COMPLETED
```

---

## 🔄 How Conditional Flow Works

### Decider Implementation

```java
@Component
public class FileValidationDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String validationStatus = stepExecution.getExecutionContext().getString("validationStatus");
        
        if ("VALID".equals(validationStatus)) {
            return new FlowExecutionStatus("VALID");
        } else {
            return new FlowExecutionStatus("INVALID");
        }
    }
}
```

### Job Configuration

```java
@Bean
Job conditionalJob(JobRepository jobRepository, FileValidationDecider fileValidationDecider, 
                   Step validateStep, Step processStep, Step reportStep, Step notificationStep) {
    return new JobBuilder("conditionalJob", jobRepository)
            .start(validateStep)
            .next(fileValidationDecider)
            .on("VALID").to(processStep)
            .from(fileValidationDecider)
            .on("INVALID").to(notificationStep)
            .from(processStep)
            .next(reportStep)
            .end()
            .build();
}
```

### Flow Control Methods

| Method | Purpose |
|--------|---------|
| `.start()` | Start with first step |
| `.next()` | Execute next step sequentially |
| `.on()` | Match exit status |
| `.to()` | Go to specific step |
| `.from()` | From previous decider |
| `.end()` | End job successfully |
| `.fail()` | End job with failure |
| `.stop()` | Stop job execution |

---

## 🎓 Learning Path

**Series Progression:**
1. Project 01 (Basic structure)
2. Project 02 (Chunk processing)
3. Project 03 (Data transformation)
4. Project 04 (Reverse flow)
5. Project 05 (Multi-step jobs)
6. Project 06 (Scheduling)
7. Project 07 (Parallel processing)
8. Project 08 (REST APIs)
9. **Project 09** ← You are here (Conditional flow)
10. Project 10 (Production-ready)

---

## 💡 Key Takeaways

✅ **Deciders** enable dynamic workflows
✅ **ExecutionContext** shares data between steps
✅ **Conditional flow** handles success and failure paths
✅ **Exit status** controls job flow
✅ **Complex business rules** supported
✅ **Error handling** with notifications

---

## 🎯 Comparison: Linear vs Conditional

| Aspect | Linear Flow | Conditional Flow |
|--------|------------|-----------------|
| **Steps** | Sequential | Dynamic |
| **Decision** | None | Decider-based |
| **Paths** | Single | Multiple |
| **Complexity** | Simple | Advanced |
| **Use Case** | Simple jobs | Complex workflows |

---

## 🐛 Troubleshooting

### Issue: "Connection refused" error
**Solution:** Ensure MySQL is running and credentials are correct

### Issue: Port 8088 already in use
**Solution:** Change port in `application.properties`:
```properties
server.port=8089
```

### Issue: Job always takes success path
**Solution:** Verify ValidateFileTasklet is setting validationStatus in ExecutionContext

### Issue: Decider not being called
**Solution:** Ensure decider is injected and configured in job builder

---

## 📈 Performance Metrics

| Metric | Value |
|--------|-------|
| Validation Time | ~100ms |
| Processing Time | ~1 second |
| Report Generation | ~500ms |
| Total Duration | ~2 seconds |

---

## 🚀 Next Steps

Move to **Project 10: Complete ETL Pipeline** to learn:
- Production-ready patterns
- Comprehensive error handling
- Monitoring and alerting
- Performance optimization
- Real-world best practices

---

## 📚 Related Projects

- **Project 01:** Hello World Job (Basic structure)
- **Project 02:** CSV to Database (Chunk processing)
- **Project 03:** Data Transformation (Validation & processing)
- **Project 04:** Database to CSV (Reverse flow)
- **Project 05:** Multi-Step Job (Multiple steps)
- **Project 06:** Scheduled Batch (Automatic scheduling)
- **Project 07:** Parallel Processing (Multi-threading)
- **Project 08:** REST API Batch (REST integration)
- **Project 09:** Conditional Flow (This project - Decision logic)

---

## 👨💻 Author

**Ananth Kumar**  
Spring Batch Learning Series - Step by Step

---

## 📄 License

This project is open source and available under the MIT License.

---

## ⭐ Support

If you found this project helpful, please give it a ⭐ on GitHub!

---

**Status:** 🟢 Production Ready

**Last Updated:** April 10, 2026
