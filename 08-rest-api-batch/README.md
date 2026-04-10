# 📦 Project 08: REST API Triggered Batch

**Difficulty:** ⭐⭐⭐⭐ Expert

---

## 🎯 What This Project Does

A Spring Batch application that exposes batch job operations through REST APIs. Demonstrates job launching, status tracking, and execution history retrieval via HTTP endpoints.

**Purpose:** Learn REST API integration with batch jobs, async execution, and job monitoring

---

## 📚 Key Concepts Learned

| Concept | Description |
|---------|-------------|
| **REST Integration** | Expose batch operations as REST endpoints |
| **JobLauncher** | Programmatically launch jobs from REST controllers |
| **JobExplorer** | Query job metadata and execution history |
| **DTOs** | Data Transfer Objects for API requests/responses |
| **Status Tracking** | Real-time job execution status monitoring |
| **Job History** | Retrieve past job executions |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                  REST API BATCH FLOW                    │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  REST Client (Postman, curl, browser)                   │
│       ↓                                                  │
│  JobController (REST endpoints)                         │
│       ↓                                                  │
│  BatchService (business logic)                          │
│       ↓                                                  │
│  JobLauncher (starts job)                               │
│       ↓                                                  │
│  Job → Step → Reader → Writer → Database                │
│       ↓                                                  │
│  JobExplorer (query status)                             │
│       ↓                                                  │
│  REST Response (JSON)                                   │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## 📂 Project Structure

```
08-rest-api-batch/
├── src/main/java/com/springbatch/restapi/
│   ├── RestApiApplication.java          # Main Spring Boot app
│   ├── config/
│   │   └── BatchConfig.java             # Job, Step, Reader, Writer config
│   ├── model/
│   │   └── Employee.java                # JPA Entity
│   ├── controller/
│   │   └── JobController.java           # REST endpoints
│   ├── service/
│   │   └── BatchService.java            # Business logic
│   └── dto/
│       ├── JobLaunchRequest.java        # Request DTO
│       └── JobStatusResponse.java       # Response DTO
├── src/main/resources/
│   ├── application.properties           # Database & server config
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
| Jackson | Latest | JSON serialization |
| Java | 17+ | Programming language |
| Maven | Latest | Build tool |

---

## 🌐 REST API Endpoints

### 1. Start Job

**Request:**
```http
POST /api/jobs/start
Content-Type: application/json

{
  "jobName": "importEmployeeJob"
}
```

**Response:**
```json
{
  "executionId": 1,
  "jobName": "importEmployeeJob",
  "status": "STARTED",
  "startTime": "2026-04-10T10:30:00",
  "endTime": null,
  "exitCode": "NOOP",
  "message": "✅ Job started successfully!"
}
```

### 2. Get Job Status

**Request:**
```http
GET /api/jobs/status/1
```

**Response:**
```json
{
  "executionId": 1,
  "jobName": "importEmployeeJob",
  "status": "COMPLETED",
  "startTime": "2026-04-10T10:30:00",
  "endTime": "2026-04-10T10:30:05",
  "exitCode": "COMPLETED",
  "message": "✅ Job status retrieved successfully!"
}
```

### 3. Get Job History

**Request:**
```http
GET /api/jobs/history
```

**Response:**
```json
[
  {
    "executionId": 1,
    "jobName": "importEmployeeJob",
    "status": "COMPLETED",
    "startTime": "2026-04-10T10:30:00",
    "endTime": "2026-04-10T10:30:05",
    "exitCode": "COMPLETED",
    "message": "Job execution record"
  },
  {
    "executionId": 2,
    "jobName": "importEmployeeJob",
    "status": "COMPLETED",
    "startTime": "2026-04-10T10:35:00",
    "endTime": "2026-04-10T10:35:03",
    "exitCode": "COMPLETED",
    "message": "Job execution record"
  }
]
```

### 4. Quick Trigger

**Request:**
```http
POST /api/jobs/trigger
```

**Response:**
```json
{
  "executionId": 3,
  "jobName": "importEmployeeJob",
  "status": "STARTED",
  "startTime": "2026-04-10T10:40:00",
  "endTime": null,
  "exitCode": "NOOP",
  "message": "✅ Job started successfully!"
}
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
server.port=8087
```

### Step 3: Build & Run

**Option A: Using Maven**
```bash
cd 08-rest-api-batch
mvn clean install
mvn spring-boot:run
```

**Option B: Using Spring Tool Suite**
1. Right-click `RestApiApplication.java`
2. Select **Run As** → **Spring Boot App**
3. Wait for: `Started RestApiApplication in X seconds`

---

## 🧪 Testing the API

### Using Postman

**1. Start Job**
- Method: `POST`
- URL: `http://localhost:8087/api/jobs/start`
- Body (JSON):
```json
{
  "jobName": "importEmployeeJob"
}
```
- Click Send → Note the `executionId`

**2. Check Status**
- Method: `GET`
- URL: `http://localhost:8087/api/jobs/status/1` (replace 1 with executionId)
- Click Send

**3. View History**
- Method: `GET`
- URL: `http://localhost:8087/api/jobs/history`
- Click Send

**4. Quick Trigger**
- Method: `POST`
- URL: `http://localhost:8087/api/jobs/trigger`
- Click Send

### Using PowerShell

```powershell
# Start job
$response = Invoke-WebRequest -Uri http://localhost:8087/api/jobs/trigger -Method POST
$response.Content | ConvertFrom-Json

# Get status (replace 1 with executionId)
Invoke-WebRequest -Uri http://localhost:8087/api/jobs/status/1 -Method GET

# Get history
Invoke-WebRequest -Uri http://localhost:8087/api/jobs/history -Method GET
```

### Using curl

```bash
# Start job
curl -X POST http://localhost:8087/api/jobs/trigger

# Get status
curl -X GET http://localhost:8087/api/jobs/status/1

# Get history
curl -X GET http://localhost:8087/api/jobs/history
```

---

## 📊 Checking Output

### 1. Clear Previous Data

```sql
TRUNCATE TABLE employee;
```

### 2. Trigger the Job

```bash
POST http://localhost:8087/api/jobs/trigger
```

### 3. Check Employee Table

```sql
USE spring_batch_db;

SELECT COUNT(*) as total_employees FROM employee;

-- Expected: 15
```

### 4. Check Job Execution History

```sql
SELECT JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, EXIT_CODE
FROM BATCH_JOB_EXECUTION
ORDER BY JOB_EXECUTION_ID DESC
LIMIT 1;

-- Expected output:
-- JOB_EXECUTION_ID | START_TIME | END_TIME | STATUS | EXIT_CODE
-- X | 2026-04-10 10:30:... | 2026-04-10 10:30:... | COMPLETED | COMPLETED
```

### 5. Check Step Execution Statistics

```sql
SELECT STEP_NAME, READ_COUNT, WRITE_COUNT, COMMIT_COUNT, STATUS
FROM BATCH_STEP_EXECUTION
WHERE JOB_EXECUTION_ID = (SELECT MAX(JOB_EXECUTION_ID) FROM BATCH_JOB_EXECUTION);

-- Expected output:
-- STEP_NAME | READ_COUNT | WRITE_COUNT | COMMIT_COUNT | STATUS
-- importEmployeeStep | 15 | 15 | 2 | COMPLETED
```

### 6. View Sample Data

```sql
SELECT id, first_name, last_name, email, department, salary 
FROM employee 
LIMIT 5;

-- Expected: 5 records
```

---

## 🔄 Processing Flow

### Step-by-Step Flow

**1. REST Request Received**
```
POST /api/jobs/trigger
```

**2. JobController Processes Request**
```
JobController.triggerJob()
  → BatchService.startJob("importEmployeeJob")
```

**3. BatchService Launches Job**
```
JobLauncher.run(importEmployeeJob, params)
  → Creates JobParameters with timestamp
  → Starts job execution
```

**4. Job Executes**
```
Job: importEmployeeJob
  → Step: importEmployeeStep
    → Reader: FlatFileItemReader (reads CSV)
    → Writer: JdbcBatchItemWriter (writes to DB)
```

**5. Execution Tracked**
```
JobExecution created
  → Status: STARTED
  → Records inserted
  → Status: COMPLETED
```

**6. Response Returned**
```json
{
  "executionId": 1,
  "jobName": "importEmployeeJob",
  "status": "COMPLETED",
  "message": "✅ Job started successfully!"
}
```

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
8. **Project 08** ← You are here (REST APIs)
9. Project 09 (Conditional flow)
10. Project 10 (Production-ready)

---

## 💡 Key Features

✅ **REST API Integration** - Trigger jobs via HTTP
✅ **JobExplorer** - Query job metadata and history
✅ **DTOs** - Clean request/response objects
✅ **Status Tracking** - Real-time job status
✅ **Execution History** - View past job runs
✅ **Error Handling** - Graceful error responses

---

## 🎯 API Response Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 400 | Bad Request |
| 404 | Not Found |
| 500 | Server Error |

---

## 🐛 Troubleshooting

### Issue: "Connection refused" error
**Solution:** Ensure MySQL is running and credentials are correct

### Issue: Port 8087 already in use
**Solution:** Change port in `application.properties`:
```properties
server.port=8088
```

### Issue: "Duplicate entry" error
**Solution:** Clear the employee table:
```sql
TRUNCATE TABLE employee;
```

### Issue: Job not found in history
**Solution:** Ensure job has completed before querying status

---

## 📈 Performance Metrics

| Metric | Value |
|--------|-------|
| CSV Records | 15 |
| Processing Time | ~2-3 seconds |
| Throughput | ~5 records/second |
| API Response Time | <100ms |

---

## 🚀 Next Steps

Move to **Project 09: Conditional Flow** to learn:
- JobExecutionDecider
- Dynamic step execution
- Retry logic
- Complex flow control

---

## 📚 Related Projects

- **Project 01:** Hello World Job (Basic structure)
- **Project 02:** CSV to Database (Chunk processing)
- **Project 03:** Data Transformation (Validation & processing)
- **Project 04:** Database to CSV (Reverse flow)
- **Project 05:** Multi-Step Job (Multiple steps)
- **Project 06:** Scheduled Batch (Automatic scheduling)
- **Project 07:** Parallel Processing (Multi-threading)
- **Project 08:** REST API Batch (This project - REST integration)

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
