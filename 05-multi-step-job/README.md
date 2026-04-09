# 📦 Project 05: Multi-Step Job

**Difficulty:** ⭐⭐⭐ Advanced

A Spring Batch application that processes employee data through multiple sequential steps, demonstrating job orchestration, step transitions, and data sharing between steps using ExecutionContext.

---

## 🎯 What This Project Does

This project demonstrates **multi-step batch processing** with data sharing:

- ✅ **Step 1:** Reads employee records from CSV file
- ✅ **Step 2:** Calculates employee statistics (count, avg salary, min/max)
- ✅ **Step 3:** Generates summary report file
- ✅ **Data Sharing:** Steps communicate via ExecutionContext
- ✅ **Sequential Flow:** Steps execute one after another
- ✅ **Metadata Tracking:** All steps tracked in database

---

## 📚 Key Concepts Learned

| Concept | Description |
|---------|-------------|
| **Multi-Step Job** | Job containing multiple sequential steps |
| **Step Transitions** | Control which step executes next |
| **ExecutionContext** | Shared data storage between steps |
| **Step Flow** | Define order and conditions for execution |
| **Job Orchestration** | Coordinate multiple steps into one workflow |
| **Data Sharing** | Pass data from one step to another |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│              MULTI-STEP JOB FLOW                        │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  Step 1: Import CSV to Database                         │
│  ├─ Read: employees.csv                                 │
│  ├─ Write: employee table                               │
│  └─ Store in ExecutionContext: recordCount = 15         │
│       ↓                                                  │
│  Step 2: Calculate Statistics                           │
│  ├─ Read: recordCount from ExecutionContext             │
│  ├─ Query: SELECT COUNT(*), AVG(salary), etc.           │
│  └─ Store in ExecutionContext: stats = {...}            │
│       ↓                                                  │
│  Step 3: Generate Summary Report                        │
│  ├─ Read: stats from ExecutionContext                   │
│  ├─ Format: Create summary.txt                          │
│  └─ Write: output/summary_report.txt                    │
│       ↓                                                  │
│  Job Completes: COMPLETED ✅                            │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## 📂 Project Structure

```
05-multi-step-job/
├── src/main/java/com/springbatch/multistep/
│   ├── MultiStepApplication.java        # Main Spring Boot app
│   ├── config/
│   │   └── BatchConfig.java             # All 3 steps + Job config
│   ├── model/
│   │   └── Employee.java                # JPA Entity
│   ├── tasklet/
│   │   ├── CalculateStatsTasklet.java   # Step 2: Calculate stats
│   │   └── GenerateReportTasklet.java   # Step 3: Generate report
│   └── controller/
│       └── BatchController.java         # REST endpoint
├── src/main/resources/
│   ├── application.properties           # Database & server config
│   ├── data/
│   │   └── employees.csv                # Sample CSV (15 records)
│   └── output/
│       └── summary_report.txt           # Generated report
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

## 📋 Step Details

### Step 1: Import CSV to Database (Chunk-Oriented)

**What it does:**
- Reads employees.csv file
- Processes in chunks of 10 records
- Writes to employee table
- Stores record count in ExecutionContext

**Components:**
- Reader: FlatFileItemReader (CSV)
- Writer: JdbcBatchItemWriter (Database)
- Chunk size: 10

**Output:**
```
✅ Step 1 Complete: 15 records imported
```

### Step 2: Calculate Statistics (Tasklet)

**What it does:**
- Reads record count from ExecutionContext (Step 1)
- Queries database for statistics
- Calculates: count, average salary, max salary, min salary
- Stores statistics in ExecutionContext

**Components:**
- Tasklet: CalculateStatsTasklet
- Data Source: JdbcTemplate
- Input: ExecutionContext (recordCount)
- Output: ExecutionContext (stats map)

**Output:**
```
✅ Statistics Calculated:
   Total Employees: 15
   Average Salary: $76,700.00
   Maximum Salary: $85,000.00
   Minimum Salary: $64,000.00
```

### Step 3: Generate Summary Report (Tasklet)

**What it does:**
- Reads statistics from ExecutionContext (Step 2)
- Formats report with employee statistics
- Writes report to file: summary_report.txt

**Components:**
- Tasklet: GenerateReportTasklet
- Input: ExecutionContext (stats)
- Output: File (summary_report.txt)

**Output:**
```
========================================
Employee Processing Summary
========================================

Total Employees: 15
Average Salary: $76,700.00
Highest Salary: $85,000.00
Lowest Salary: $64,000.00
Processed Date: 2026-04-09 11:43:38

========================================
```

---

## 💡 ExecutionContext - Data Sharing Between Steps

### How It Works

ExecutionContext is a Map that persists across steps in a job.

**Step 1 - Store Data:**
```java
ExecutionContext context = chunkContext.getStepContext().getJobExecutionContext();
context.put("recordCount", 15);  // Store data
```

**Step 2 - Read and Store:**
```java
ExecutionContext context = chunkContext.getStepContext().getJobExecutionContext();
int count = (int) context.get("recordCount");  // Retrieve data from Step 1
context.put("stats", statsMap);  // Store new data for Step 3
```

**Step 3 - Read Data:**
```java
ExecutionContext context = chunkContext.getStepContext().getJobExecutionContext();
Map stats = (Map) context.get("stats");  // Retrieve data from Step 2
```

### Benefits

✅ **Data Sharing:** Pass data between steps without database
✅ **Efficiency:** No extra database queries
✅ **Simplicity:** Simple Map-like interface
✅ **Persistence:** Data persists across step boundaries

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
spring.datasource.password=8520
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Spring Batch Configuration
spring.batch.job.enabled=false
spring.batch.jdbc.initialize-schema=always

# Server Configuration
server.port=8084
```

### Step 3: Build & Run

**Option A: Using Maven**
```bash
cd 05-multi-step-job
mvn clean install
mvn spring-boot:run
```

**Option B: Using Spring Tool Suite**
1. Right-click `MultiStepApplication.java`
2. Select **Run As** → **Spring Boot App**
3. Wait for: `Started MultiStepApplication in X seconds`

---

## 🧪 Testing & Triggering the Job

### Trigger the Multi-Step Job

**Using PowerShell (Windows):**
```powershell
Invoke-WebRequest -Uri http://localhost:8084/api/batch/process-employees -Method POST
```

**Using curl (Linux/Mac):**
```bash
curl -X POST http://localhost:8084/api/batch/process-employees
```

**Using Postman:**
- Method: `POST`
- URL: `http://localhost:8084/api/batch/process-employees`
- Click Send

**Expected Response:**
```
✅ Multi-Step Employee Processing Job executed successfully! Check output/summary_report.txt
```

---

## 📊 Checking Output

### 1. Console Output

Watch the application console for processing logs:

```
========================================
📊 Step 1: Importing CSV to Database
========================================

Executing step: [importEmployeeStep]
Reading resource: [class path resource [data/employees.csv]]
Chunk 1: Read 10 records
Chunk 1: Written 10 records
Chunk 2: Read 5 records
Chunk 2: Written 5 records
✅ Step 1 Complete: 15 records imported

========================================
📊 Step 2: Calculating Statistics
========================================

📌 Records imported in Step 1: 15
✅ Statistics Calculated:
   Total Employees: 15
   Average Salary: $76,700.00
   Maximum Salary: $85,000.00
   Minimum Salary: $64,000.00

========================================
📊 Step 3: Generating Summary Report
========================================

✅ Report Generated Successfully!
📁 File Location: src/main/resources/output/summary_report.txt

========================================
Employee Processing Summary
========================================

Total Employees: 15
Average Salary: $76,700.00
Highest Salary: $85,000.00
Lowest Salary: $64,000.00
Processed Date: 2026-04-09 11:43:38

========================================
```

### 2. Check Generated Report File

**Location:** `src/main/resources/output/summary_report.txt`

**Expected Content:**
```
========================================
Employee Processing Summary
========================================

Total Employees: 15
Average Salary: $76,700.00
Highest Salary: $85,000.00
Lowest Salary: $64,000.00
Processed Date: 2026-04-09 11:43:38

========================================
```

### 3. Database Query - Check Employee Table

```sql
SELECT * FROM employee;

-- Expected: 15 records inserted
```

### 4. Database Query - Check Job Execution

```sql
SELECT JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, EXIT_CODE
FROM BATCH_JOB_EXECUTION
ORDER BY JOB_EXECUTION_ID DESC
LIMIT 1;

-- Expected output:
-- JOB_EXECUTION_ID | START_TIME | END_TIME | STATUS | EXIT_CODE
-- X | 2026-04-09 11:43:38 | 2026-04-09 11:43:40 | COMPLETED | COMPLETED
```

### 5. Database Query - Check Step Execution

```sql
SELECT STEP_NAME, READ_COUNT, WRITE_COUNT, COMMIT_COUNT, STATUS
FROM BATCH_STEP_EXECUTION
WHERE JOB_EXECUTION_ID = (SELECT MAX(JOB_EXECUTION_ID) FROM BATCH_JOB_EXECUTION)
ORDER BY STEP_EXECUTION_ID;

-- Expected output:
-- STEP_NAME | READ_COUNT | WRITE_COUNT | COMMIT_COUNT | STATUS
-- importEmployeeStep | 15 | 15 | 2 | COMPLETED
-- calculateStatsStep | 0 | 0 | 1 | COMPLETED
-- generateReportStep | 0 | 0 | 1 | COMPLETED
```

---

## 🔄 Processing Flow Example

### Detailed Internal Flow

**1. Job Starts**
```
JobLauncher triggers employeeProcessingJob
JobParameters: {startTime: 1712748600000}
ExecutionContext created (empty)
```

**2. Step 1: Import CSV to Database**

**Chunk 1 (Records 1-10):**
```
1. FlatFileItemReader opens: employees.csv
2. Skips header line
3. Reads lines 1-10
4. LineMapper converts each line to Employee object
5. Chunk buffer: List<Employee> with 10 objects
6. JdbcBatchItemWriter receives 10 Employee objects
7. Executes: INSERT INTO employee VALUES (1,...), (2,...), ..., (10,...)
8. Transaction commits ✅
9. Chunk buffer cleared
```

**Chunk 2 (Records 11-15):**
```
1. Reads lines 11-15
2. LineMapper converts to Employee objects
3. Chunk buffer: List<Employee> with 5 objects
4. JdbcBatchItemWriter receives 5 Employee objects
5. Executes: INSERT INTO employee VALUES (11,...), ..., (15,...)
6. Transaction commits ✅
7. Reader returns null (EOF)
```

**Step 1 Completion:**
```
StepExecutionListener stores in ExecutionContext:
  context.put("recordCount", 15)
Step 1 Status: COMPLETED ✅
```

**3. Step 2: Calculate Statistics**

**Tasklet Execution:**
```
1. CalculateStatsTasklet.execute() called
2. Reads from ExecutionContext: recordCount = 15
3. Executes SQL queries:
   - SELECT COUNT(*) FROM employee → 15
   - SELECT AVG(salary) FROM employee → 76700.0
   - SELECT MAX(salary) FROM employee → 85000.0
   - SELECT MIN(salary) FROM employee → 64000.0
4. Creates statistics map:
   {
     "totalCount": 15,
     "avgSalary": 76700.0,
     "maxSalary": 85000.0,
     "minSalary": 64000.0
   }
5. Stores in ExecutionContext: context.put("stats", statsMap)
6. Returns RepeatStatus.FINISHED
Step 2 Status: COMPLETED ✅
```

**4. Step 3: Generate Summary Report**

**Tasklet Execution:**
```
1. GenerateReportTasklet.execute() called
2. Reads from ExecutionContext: stats map
3. Extracts statistics:
   - totalCount = 15
   - avgSalary = 76700.0
   - maxSalary = 85000.0
   - minSalary = 64000.0
4. Gets current date/time
5. Formats report:
   ========================================
   Employee Processing Summary
   ========================================
   
   Total Employees: 15
   Average Salary: $76,700.00
   Highest Salary: $85,000.00
   Lowest Salary: $64,000.00
   Processed Date: 2026-04-09 11:43:38
   
   ========================================
6. Writes to file: src/main/resources/output/summary_report.txt
7. Returns RepeatStatus.FINISHED
Step 3 Status: COMPLETED ✅
```

**5. Job Completes**
```
All steps completed successfully
Job Status: COMPLETED ✅
Total duration: ~5 seconds
```

---

## 💡 Key Takeaways

✅ **Multi-Step Jobs** enable complex workflows
✅ **ExecutionContext** enables data sharing between steps
✅ **Step Transitions** control job flow
✅ **Chunk-Oriented + Tasklet** can be combined
✅ **Sequential Processing** ensures order of execution
✅ **Job Orchestration** coordinates multiple operations

---

## 🎯 Comparison: Single-Step vs Multi-Step

| Aspect | Project 02-04 (Single) | Project 05 (Multi) |
|--------|----------------------|-------------------|
| **Steps** | 1 | 3 |
| **Flow** | CSV → DB | CSV → DB → Stats → Report |
| **Data Sharing** | None | ExecutionContext |
| **Complexity** | Simple | Advanced |
| **Use Case** | Simple ETL | Complex workflows |
| **Coordination** | None | Job orchestration |

---

## 🐛 Troubleshooting

### Issue: "Table 'employee' doesn't exist"
**Solution:** Create the table using the SQL script in Step 1 of Setup Instructions

### Issue: "Connection refused" error
**Solution:** Ensure MySQL is running and credentials in `application.properties` are correct

### Issue: Port 8084 already in use
**Solution:** Change port in `application.properties`:
```properties
server.port=8085
```

### Issue: Report file not created
**Solution:** Ensure `src/main/resources/output/` directory exists and is writable

### Issue: ExecutionContext returns null
**Solution:** Verify Step 1 completed successfully before Step 2 executes

---

## 📈 Performance Metrics

| Metric | Value |
|--------|-------|
| CSV Records Read | 15 |
| Database Records Written | 15 |
| Chunks Processed | 2 |
| Statistics Calculated | 4 (count, avg, max, min) |
| Report Generated | 1 file |
| Total Processing Time | ~5 seconds |
| Throughput | ~3 records/second |

---

## 🚀 Next Steps

After mastering this project, explore:

1. **Project 06: Scheduled Batch** - Automatic job scheduling
2. **Project 07: Parallel Processing** - Multi-threaded batch jobs
3. **Project 08: REST API Batch** - Advanced REST integration
4. **Project 09: Conditional Flow** - Decision logic and retry

---

## 📚 Related Projects

- **Project 01:** Hello World Job (Basic structure)
- **Project 02:** CSV to Database (Chunk processing)
- **Project 03:** Data Transformation (Validation & processing)
- **Project 04:** Database to CSV (Reverse flow)
- **Project 05:** Multi-Step Job (This project - Multiple steps)

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

**Last Updated:** April 9, 2026
