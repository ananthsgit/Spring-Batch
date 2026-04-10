# 📦 Project 06: Scheduled Batch Job

**Difficulty:** ⭐⭐⭐ Advanced

---

## 🎯 What This Project Does

A Spring Batch application that automatically imports employee data from CSV to database on a scheduled basis using cron expressions.

**Purpose:** Learn job scheduling, cron expressions, and automatic batch execution

---

## 📚 Key Concepts Learned

| Concept | Description |
|---------|-------------|
| **@Scheduled** | Spring annotation to run methods at fixed intervals |
| **Cron Expressions** | Define complex schedules (daily, weekly, monthly) |
| **JobParameters** | Make each job execution unique (timestamp, run ID) |
| **@EnableScheduling** | Enable Spring's scheduling capability |
| **Automatic Execution** | Jobs run without manual intervention |

---

## 🏗️ Architecture

```
Spring Scheduler (Cron Trigger)
    ↓
BatchScheduler.runDailyImport()
    ↓
JobLauncher
    ↓
Job (importEmployeeJob)
    ↓
Step (importEmployeeStep)
    ↓
Reader (CSV) → Writer (Database)
    ↓
Employee Table
```

---

## 📂 Project Structure

```
06-scheduled-batch/
├── src/main/java/com/springbatch/scheduled/
│   ├── ScheduledApplication.java        # Main Spring Boot app with @EnableScheduling
│   ├── config/
│   │   └── BatchConfig.java             # Job, Step, Reader, Writer config
│   ├── model/
│   │   └── Employee.java                # JPA Entity
│   ├── scheduler/
│   │   └── BatchScheduler.java          # @Scheduled method for automatic execution
│   └── controller/
│       └── BatchController.java         # REST endpoint for manual testing
├── src/main/resources/
│   ├── application.properties           # Database & cron schedule config
│   └── data/
│       └── employees.csv                # Sample CSV (15 records)
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

## ⏰ Cron Expression Examples

| Expression | Schedule |
|-----------|----------|
| `0 0 0 * * ?` | Every day at midnight |
| `0 0 9 * * MON-FRI` | Weekdays at 9 AM |
| `0 0 */6 * * ?` | Every 6 hours |
| `0 0 0 1 * ?` | First day of every month |
| `0 */5 * * * ?` | Every 5 minutes |
| `0 30 2 * * ?` | Every day at 2:30 AM |

---

## ⚙️ Setup Instructions

### Prerequisites
- Java 17 or higher
- MySQL 8.x
- Maven 3.6+
- Spring Tool Suite or any IDE

### Step 1: Database Setup

Create the database and table:

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
server.port=8085

# Batch Scheduling Configuration
# Cron: 0 0 0 * * ? = Every day at midnight
batch.schedule.cron=0 0 0 * * ?
```

### Step 3: Build & Run

**Option A: Using Maven**
```bash
cd 06-scheduled-batch
mvn clean install
mvn spring-boot:run
```

**Option B: Using Spring Tool Suite**
1. Right-click `ScheduledApplication.java`
2. Select **Run As** → **Spring Boot App**
3. Wait for: `Started ScheduledApplication in X seconds`

---

## 🧪 Testing & Triggering the Job

### Automatic Execution (Scheduled)

The job runs automatically based on the cron expression in `application.properties`:

```properties
batch.schedule.cron=0 0 0 * * ?  # Daily at midnight
```

Watch the console for execution logs.

### Manual Testing (REST API)

Trigger the job manually for testing:

**Using PowerShell (Windows):**
```powershell
Invoke-WebRequest -Uri http://localhost:8085/api/batch/trigger-now -Method POST
```

**Using curl (Linux/Mac):**
```bash
curl -X POST http://localhost:8085/api/batch/trigger-now
```

**Using Postman:**
- Method: `POST`
- URL: `http://localhost:8085/api/batch/trigger-now`
- Click Send

**Expected Response:**
```
✅ Job triggered successfully!
```

---

## 📊 Checking Output

### 1. Clear Previous Data (First Time Only)

```sql
TRUNCATE TABLE employee;
```

### 2. Trigger the Job

```bash
POST http://localhost:8085/api/batch/trigger-now
```

### 3. Check Employee Table

```sql
USE spring_batch_db;

SELECT * FROM employee;

-- Expected: 15 records inserted
```

**Expected Result:**
```
+----+------------+-----------+--------------------------------+------------+--------+
| id | first_name | last_name | email                          | department | salary |
+----+------------+-----------+--------------------------------+------------+--------+
|  1 | John       | Doe       | john.doe@example.com           | IT         |  75000 |
|  2 | Jane       | Smith     | jane.smith@example.com         | HR         |  65000 |
|  3 | Mike       | Johnson   | mike.johnson@example.com       | Finance    |  80000 |
...
| 15 | Charles    | Harris    | charles.harris@example.com     | Finance    |  83000 |
+----+------------+-----------+--------------------------------+------------+--------+
15 rows in set
```

### 4. Check Job Execution History

```sql
SELECT JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, EXIT_CODE
FROM BATCH_JOB_EXECUTION
ORDER BY JOB_EXECUTION_ID DESC
LIMIT 1;

-- Expected output:
-- JOB_EXECUTION_ID | START_TIME | END_TIME | STATUS | EXIT_CODE
-- X | 2026-04-10 09:50:... | 2026-04-10 09:50:... | COMPLETED | COMPLETED
```

### 5. Check Step Execution Statistics

```sql
SELECT STEP_NAME, READ_COUNT, WRITE_COUNT, COMMIT_COUNT, STATUS
FROM BATCH_STEP_EXECUTION
WHERE JOB_EXECUTION_ID = (SELECT MAX(JOB_EXECUTION_ID) FROM BATCH_JOB_EXECUTION);

-- Expected output:
-- STEP_NAME | READ_COUNT | WRITE_COUNT | COMMIT_COUNT | STATUS
-- importEmployeeStep | 15 | 15 | 2 | COMPLETED

-- Analysis:
-- READ_COUNT: 15 (read all 15 records from CSV)
-- WRITE_COUNT: 15 (wrote 15 records to database)
-- COMMIT_COUNT: 2 (2 chunks processed: 10 + 5)
-- STATUS: COMPLETED (job finished successfully)
```

### 6. Verify Employee Count

```sql
SELECT COUNT(*) as total_employees FROM employee;

-- Expected: 15
```

### 7. Console Output

Watch the IDE console for these logs:

```
✅ Scheduled job executed at: 2026-04-10 09:50:10
Job: [SimpleJob: [name=importEmployeeJob]] launched with the following parameters
Executing step: [importEmployeeStep]
Step: [importEmployeeStep] executed in XXXms
Job: [SimpleJob: [name=importEmployeeJob]] completed with the following status: [COMPLETED]
```

---

## 🔄 Processing Flow Example

### Detailed Internal Flow

**1. Scheduler Triggers (Based on Cron)**
```
Cron expression matches current time (e.g., midnight)
BatchScheduler.runDailyImport() method executes
```

**2. JobParameters Created**
```java
JobParameters params = new JobParametersBuilder()
    .addLong("time", System.currentTimeMillis())
    .toJobParameters();
```
- Timestamp makes each run unique
- Prevents duplicate job instances

**3. JobLauncher Starts Job**
```
jobLauncher.run(importEmployeeJob, params)
```

**4. Chunk 1 Processing (Records 1-10)**
```
Reader: Reads lines 1-10 from employees.csv
Mapper: Converts CSV lines to Employee objects
Chunk buffer: List<Employee> with 10 objects
Writer: Batch inserts 10 records to database
Transaction commits ✅
```

**5. Chunk 2 Processing (Records 11-15)**
```
Reader: Reads lines 11-15 from employees.csv
Mapper: Converts CSV lines to Employee objects
Chunk buffer: List<Employee> with 5 objects
Writer: Batch inserts 5 records to database
Transaction commits ✅
Reader returns null (EOF)
```

**6. Step Completes**
```
Status: COMPLETED
READ_COUNT: 15
WRITE_COUNT: 15
COMMIT_COUNT: 2
```

**7. Job Completes**
```
Status: COMPLETED
Exit Code: COMPLETED
Next scheduled execution: Tomorrow at midnight
```

---

## 🎓 Learning Path

**Series Progression:**
1. Project 01 (Basic structure)
2. Project 02 (Chunk processing)
3. Project 03 (Data transformation)
4. Project 04 (Reverse flow)
5. Project 05 (Multi-step jobs)
6. **Project 06** ← You are here (Scheduling)
7. Project 07 (Parallel processing)
8. Project 08 (REST APIs)
9. Project 09 (Conditional flow)
10. Project 10 (Production-ready)

---

## 💡 Key Takeaways

✅ **@Scheduled** automates job execution based on cron expressions
✅ **@EnableScheduling** enables Spring's scheduling capability
✅ **JobParameters** with timestamp prevent duplicate runs
✅ **Cron expressions** provide flexible scheduling options
✅ **Manual trigger** endpoint useful for testing
✅ **Batch metadata** tracks all scheduled executions
✅ **Idempotent jobs** can be safely re-run

---

## 🎯 Comparison: Manual vs Scheduled

| Aspect | Manual (Project 01) | Scheduled (Project 06) |
|--------|-------------------|----------------------|
| **Trigger** | REST API call | Cron expression |
| **Execution** | On-demand | Automatic |
| **Frequency** | Manual | Periodic |
| **Use Case** | Ad-hoc jobs | Daily/weekly reports |
| **Configuration** | None | Cron expression |

---

## 🐛 Troubleshooting

### Issue: Job not running at scheduled time
**Solution:** Verify cron expression is correct and application is running

### Issue: "Duplicate entry" error
**Solution:** Clear the employee table before re-running:
```sql
TRUNCATE TABLE employee;
```

### Issue: "Connection refused" error
**Solution:** Ensure MySQL is running and credentials in `application.properties` are correct

### Issue: Port 8085 already in use
**Solution:** Change port in `application.properties`:
```properties
server.port=8086
```

### Issue: CSV file not found
**Solution:** Ensure `employees.csv` is in `src/main/resources/data/`

---

## 📈 Performance Metrics

| Metric | Value |
|--------|-------|
| CSV Records Read | 15 |
| Database Records Written | 15 |
| Processing Time | ~2-3 seconds |
| Throughput | ~5 records/second |
| Chunks Processed | 2 |
| Transactions Committed | 2 |

---

## 🚀 Next Steps

Move to **Project 07: Parallel Processing** to learn:
- Multi-threaded steps
- Partitioning
- Performance optimization
- Processing large datasets efficiently

---

## 📚 Related Projects

- **Project 01:** Hello World Job (Basic structure)
- **Project 02:** CSV to Database (Chunk processing)
- **Project 03:** Data Transformation (Validation & processing)
- **Project 04:** Database to CSV (Reverse flow)
- **Project 05:** Multi-Step Job (Multiple steps)
- **Project 06:** Scheduled Batch (This project - Automatic scheduling)

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
