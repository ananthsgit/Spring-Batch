# 📦 Project 03: Data Transformation

**Difficulty:** ⭐⭐ Intermediate

---

## 🎯 Learning Objectives

- Transform data during processing
- Validate records before writing
- Skip invalid records
- Apply business logic in ItemProcessor
- Handle errors gracefully

---

## 📚 Key Concepts

### 1. **ItemProcessor**
Transforms/validates data between reading and writing.

### 2. **Validation**
Check data integrity (email format, salary range, etc.).

### 3. **Skip Logic**
Continue processing even if some records fail.

### 4. **Data Transformation**
Convert data (uppercase names, calculate bonuses, etc.).

---

## 🏗️ Architecture

```
CSV → Reader → Processor (Validate + Transform) → Writer → Database
                    ↓
              Skip Invalid Records
```

---

## 📂 Project Structure

```
03-data-transformation/
├── src/main/java/com/springbatch/transform/
│   ├── config/
│   │   └── BatchConfig.java
│   ├── model/
│   │   └── Employee.java
│   ├── processor/
│   │   └── EmployeeProcessor.java    # Validation & transformation
│   ├── listener/
│   │   └── SkipListener.java         # Log skipped records
│   ├── controller/
│   │   └── BatchController.java
│   └── TransformApplication.java
├── src/main/resources/
│   ├── application.properties
│   └── data/
│       └── employees.csv
└── pom.xml
```

---

## 🔧 Transformation Logic

### Example Transformations:
- Convert names to UPPERCASE
- Validate email format
- Check salary > 0
- Calculate bonus (10% of salary)
- Skip records with invalid data

---

## ⚙️ Setup Instructions

### 1. Database Setup
```sql
CREATE TABLE employee (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    department VARCHAR(50),
    salary DECIMAL(10,2),
    bonus DECIMAL(10,2)
);
```

### 2. Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

---

## 🌐 REST API Endpoints

The application exposes REST endpoints to trigger and monitor the batch job. Use these endpoints to integrate the batch process into your application or test it programmatically.

### Endpoints Overview

| Endpoint | Method | Description | Port |
|----------|--------|-------------|------|
| `/batch/run` | `POST` | Trigger the data transformation job | 8080 |
| `/batch/status/{jobExecutionId}` | `GET` | Get job execution status | 8080 |
| `/batch/all-jobs` | `GET` | List all job executions | 8080 |
| `/batch/last-execution` | `GET` | Get the last job execution details | 8080 |
| `/employee/count` | `GET` | Get total processed employees count | 8080 |

### 1. Trigger Data Transformation Job

**Endpoint:** `POST http://localhost:8080/batch/run`

**Description:** Starts a new batch job that reads employees from CSV, validates, transforms, and writes to database.

**Request Example:**
```bash
curl -X POST http://localhost:8080/batch/run
```

**Expected Response (Success):**
```json
{
  "message": "Batch job started successfully",
  "jobExecutionId": 1,
  "jobName": "transformationJob",
  "startTime": "2026-04-08T10:30:00",
  "status": "STARTED"
}
```

**Expected Response (if job already running):**
```json
{
  "error": "Batch job is already running",
  "status": 409
}
```

---

### 2. Get Job Execution Status

**Endpoint:** `GET http://localhost:8080/batch/status/1`

**Description:** Retrieves the status of a specific batch job execution by its ID.

**Request Example:**
```bash
curl -X GET http://localhost:8080/batch/status/1
```

**Expected Response (Job Completed):**
```json
{
  "jobExecutionId": 1,
  "jobName": "transformationJob",
  "status": "COMPLETED",
  "startTime": "2026-04-08T10:30:00",
  "endTime": "2026-04-08T10:30:15",
  "duration": "15 seconds",
  "exitCode": "COMPLETED",
  "exitMessage": "Job completed successfully",
  "stepExecutions": [
    {
      "stepName": "transformationStep",
      "status": "COMPLETED",
      "readCount": 4,
      "writeCount": 2,
      "skipCount": 2,
      "failureCount": 0
    }
  ]
}
```

**Expected Response (Job Running):**
```json
{
  "jobExecutionId": 1,
  "jobName": "transformationJob",
  "status": "STARTED",
  "startTime": "2026-04-08T10:30:00",
  "endTime": null,
  "duration": "In progress",
  "exitCode": null,
  "exitMessage": null
}
```

---

### 3. List All Job Executions

**Endpoint:** `GET http://localhost:8080/batch/all-jobs`

**Description:** Retrieves a summary of all batch job executions.

**Request Example:**
```bash
curl -X GET http://localhost:8080/batch/all-jobs
```

**Expected Response:**
```json
{
  "totalExecutions": 3,
  "executions": [
    {
      "jobExecutionId": 1,
      "jobName": "transformationJob",
      "status": "COMPLETED",
      "startTime": "2026-04-08T10:30:00",
      "endTime": "2026-04-08T10:30:15",
      "readCount": 4,
      "writeCount": 2,
      "skipCount": 2
    },
    {
      "jobExecutionId": 2,
      "jobName": "transformationJob",
      "status": "COMPLETED",
      "startTime": "2026-04-08T11:00:00",
      "endTime": "2026-04-08T11:00:12",
      "readCount": 4,
      "writeCount": 2,
      "skipCount": 2
    },
    {
      "jobExecutionId": 3,
      "jobName": "transformationJob",
      "status": "FAILED",
      "startTime": "2026-04-08T11:30:00",
      "endTime": "2026-04-08T11:30:05",
      "readCount": 2,
      "writeCount": 1,
      "skipCount": 0
    }
  ]
}
```

---

### 4. Get Last Job Execution Details

**Endpoint:** `GET http://localhost:8080/batch/last-execution`

**Description:** Retrieves detailed information about the most recent batch job execution.

**Request Example:**
```bash
curl -X GET http://localhost:8080/batch/last-execution
```

**Expected Response:**
```json
{
  "jobExecutionId": 3,
  "jobName": "transformationJob",
  "status": "COMPLETED",
  "startTime": "2026-04-08T11:45:00",
  "endTime": "2026-04-08T11:45:18",
  "duration": "18 seconds",
  "exitCode": "COMPLETED",
  "exitMessage": "Job completed successfully",
  "readCount": 4,
  "writeCount": 2,
  "skipCount": 2,
  "failureCount": 0,
  "stepExecutions": [
    {
      "stepName": "transformationStep",
      "status": "COMPLETED",
      "readCount": 4,
      "writeCount": 2,
      "skipCount": 2,
      "skippedRecords": [
        {
          "reason": "Invalid email format",
          "record": "2,Jane,Smith,invalid-email,HR,65000"
        },
        {
          "reason": "Salary cannot be negative or zero",
          "record": "3,Mike,Johnson,mike@example.com,Finance,-5000"
        }
      ]
    }
  ]
}
```

---

### 5. Get Processed Employee Count

**Endpoint:** `GET http://localhost:8080/employee/count`

**Description:** Retrieves the total count of employees successfully processed and stored in the database.

**Request Example:**
```bash
curl -X GET http://localhost:8080/employee/count
```

**Expected Response:**
```json
{
  "totalEmployees": 2,
  "message": "Total employees in database",
  "lastUpdated": "2026-04-08T11:45:18"
}
```

---

### Testing the API with PowerShell (Windows)

If you prefer to test using PowerShell instead of curl, use these commands:

**Trigger job:**
```powershell
$response = Invoke-WebRequest -Uri "http://localhost:8080/batch/run" -Method POST
$response.Content | ConvertFrom-Json | Format-List
```

**Get job status:**
```powershell
$response = Invoke-WebRequest -Uri "http://localhost:8080/batch/status/1" -Method GET
$response.Content | ConvertFrom-Json | Format-List
```

**Get last execution:**
```powershell
$response = Invoke-WebRequest -Uri "http://localhost:8080/batch/last-execution" -Method GET
$response.Content | ConvertFrom-Json | Format-List
```

---

## 🧪 Testing

### Sample CSV with Invalid Data:
```csv
1,John,Doe,john@example.com,IT,75000
2,Jane,Smith,invalid-email,HR,65000    ← Invalid email
3,Mike,Johnson,mike@example.com,Finance,-5000  ← Negative salary
4,Sarah,Williams,sarah@example.com,IT,80000
```

### Expected Behavior:
- Record 1: ✅ Processed (JOHN DOE, bonus: 7500)
- Record 2: ⚠️ Skipped (invalid email)
- Record 3: ⚠️ Skipped (negative salary)
- Record 4: ✅ Processed (SARAH WILLIAMS, bonus: 8000)

---

## 🔍 What Happens Internally?

1. Reader reads CSV record
2. Processor validates data
3. If valid → Transform and return
4. If invalid → Return null (skip)
5. Writer writes only valid records
6. SkipListener logs skipped records
7. Job completes with skip count

---

## 💡 Key Takeaways

✅ ItemProcessor enables data transformation  
✅ Return null from processor to skip records  
✅ SkipListener tracks failed records  
✅ Business logic belongs in processor  
✅ Validation prevents bad data in database  

---

## 🚀 Next Steps

Move to **Project 04: Database to CSV** to learn:
- JdbcCursorItemReader
- FlatFileItemWriter
- Reverse data flow (DB → File)

---

**Status:** 🟢 Ready for implementation
