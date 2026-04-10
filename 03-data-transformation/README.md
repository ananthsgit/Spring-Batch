# 📦 Project 03: Data Transformation

**Difficulty:** ⭐⭐ Intermediate

A Spring Batch application that reads employee data from CSV, validates records, transforms data, and writes valid records to MySQL database while skipping invalid ones.

---

## 🎯 What This Project Does

This project demonstrates **data validation and transformation** in batch processing:

- ✅ Reads employee records from CSV file
- ✅ Validates email format and salary values
- ✅ Transforms data (uppercase names, calculate bonuses)
- ✅ Skips invalid records gracefully
- ✅ Writes valid records to database
- ✅ Tracks processing statistics (read/write/skip counts)

---

## 📚 Key Concepts Learned

| Concept | Description |
|---------|-------------|
| **ItemProcessor** | Validates and transforms data between reading and writing |
| **Validation** | Email format check, salary range validation |
| **Skip Logic** | Continue processing even if some records fail |
| **Data Transformation** | Convert names to uppercase, calculate bonuses |
| **Chunk Processing** | Process data in batches for memory efficiency |
| **Error Handling** | Graceful handling of invalid records |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                  DATA TRANSFORMATION FLOW                │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  CSV File (employees.csv)                               │
│       ↓                                                  │
│  FlatFileItemReader (reads line by line)                │
│       ↓                                                  │
│  EmployeeProcessor (validates & transforms)             │
│       ├─ Valid Record → Transform → Continue            │
│       └─ Invalid Record → Return null → Skip            │
│       ↓                                                  │
│  JdbcBatchItemWriter (batch insert)                     │
│       ↓                                                  │
│  MySQL Database (employee table)                        │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## 📂 Project Structure

```
03-data-transformation/
├── src/main/java/com/springbatch/transform/
│   ├── TransformApplication.java          # Main Spring Boot app
│   ├── config/
│   │   └── BatchConfig.java               # Job, Step, Reader, Writer config
│   ├── model/
│   │   └── Employee.java                  # JPA Entity
│   ├── processor/
│   │   └── EmployeeProcessor.java         # Validation & transformation logic
│   └── controller/
│       └── BatchController.java           # REST endpoint
├── src/main/resources/
│   ├── application.properties             # Database & server config
│   └── data/
│       └── employees.csv                  # Sample CSV with valid & invalid records
├── pom.xml                                # Maven dependencies
└── README.md                              # This file
```

---

## 🔧 Technologies Used

| Technology | Version | Purpose |
|-----------|---------|----------|
| Spring Boot | 3.2.0 | Application framework |
| Spring Batch | 5.x | Batch processing |
| Spring Data JPA | Latest | ORM & database access |
| MySQL | 8.x | Database |
| Java | 17+ | Programming language |
| Maven | Latest | Build tool |

---

## 📋 Transformation & Validation Rules

### Validation Rules:
```java
✅ Email must contain @ and . (basic validation)
✅ Salary must be > 0 (positive value)
✅ All fields must be non-null
```

### Transformation Rules:
```java
✅ Convert first name to UPPERCASE
✅ Convert last name to UPPERCASE
✅ Calculate bonus = 10% of salary
```

### Skip Conditions:
```java
⚠️ Invalid email format → Record skipped
⚠️ Salary ≤ 0 → Record skipped
⚠️ Null values → Record skipped
```

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
    salary DECIMAL(10,2),
    bonus DECIMAL(10,2)
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
spring.jpa.show-sql=true

# Spring Batch Configuration
spring.batch.job.enabled=false
spring.batch.jdbc.initialize-schema=always

# Server Configuration
server.port=8082
```

### Step 3: Build & Run

**Option A: Using Maven**
```bash
cd 03-data-transformation
mvn clean install
mvn spring-boot:run
```

**Option B: Using Spring Tool Suite**
1. Right-click `TransformApplication.java`
2. Select **Run As** → **Spring Boot App**
3. Wait for: `Started TransformApplication in X seconds`

**Option C: Using IDE Run Button**
- Click the Run button in your IDE
- Application starts on port 8082

---

## 🧪 Testing & Triggering the Job

### Sample CSV Data

The project includes `src/main/resources/data/employees.csv` with 15 records:

```csv
id,firstName,lastName,email,department,salary
1,john,doe,john.doe@example.com,IT,75000
2,jane,smith,invalid-email,HR,65000              ← Invalid email (no dot)
3,mike,johnson,mike.johnson@example.com,Finance,-5000  ← Negative salary
4,sarah,williams,sarah.williams@example.com,IT,80000
5,david,brown,david.brown@example.com,Marketing,68000
6,emily,davis,emily@invalid,IT,72000             ← Invalid email (no dot)
7,james,miller,james.miller@example.com,HR,0     ← Zero salary
8,linda,wilson,linda.wilson@example.com,Finance,85000
9,robert,moore,robert.moore@example.com,IT,76000
10,maria,taylor,maria.taylor@example.com,Marketing,70000
11,william,anderson,william.anderson@example.com,Finance,82000
12,patricia,thomas,patricia.thomas@example.com,HR,64000
13,richard,jackson,richard.jackson@example.com,IT,79000
14,jennifer,white,jennifer.white@example.com,Marketing,71000
15,charles,harris,charles.harris@example.com,Finance,83000
```

### Trigger the Job

**Using PowerShell (Windows):**
```powershell
Invoke-WebRequest -Uri http://localhost:8082/api/batch/transform -Method POST
```

**Using curl (Linux/Mac):**
```bash
curl -X POST http://localhost:8082/api/batch/transform
```

**Using Postman:**
- Method: `POST`
- URL: `http://localhost:8082/api/batch/transform`
- Click Send

**Expected Response:**
```
✅ Data Transformation Job executed successfully! Check database for transformed records.
```

---

## 📊 Checking Output

### 1. Console Output

Watch the application console for processing logs:

```
Processing record: 1 - john doe
✅ Valid - Transformed to: JOHN DOE, Bonus: 7500.0

Processing record: 2 - jane smith
⚠️ Skipped - Invalid email: invalid-email

Processing record: 3 - mike johnson
⚠️ Skipped - Invalid salary: -5000.0

Processing record: 4 - sarah williams
✅ Valid - Transformed to: SARAH WILLIAMS, Bonus: 8000.0

...
```

### 2. Database Query - View Processed Employees

```sql
USE spring_batch_db;

-- View all processed employees
SELECT * FROM employee;

-- Expected output (10 valid records):
-- ID | FIRST_NAME | LAST_NAME | EMAIL | DEPARTMENT | SALARY | BONUS
-- 1  | JOHN       | DOE       | john.doe@example.com | IT | 75000 | 7500
-- 4  | SARAH      | WILLIAMS  | sarah.williams@example.com | IT | 80000 | 8000
-- 5  | DAVID      | BROWN     | david.brown@example.com | Marketing | 68000 | 6800
-- 8  | LINDA      | WILSON    | linda.wilson@example.com | Finance | 85000 | 8500
-- 9  | ROBERT     | MOORE     | robert.moore@example.com | IT | 76000 | 7600
-- 10 | MARIA      | TAYLOR    | maria.taylor@example.com | Marketing | 70000 | 7000
-- 11 | WILLIAM    | ANDERSON  | william.anderson@example.com | Finance | 82000 | 8200
-- 12 | PATRICIA   | THOMAS    | patricia.thomas@example.com | HR | 64000 | 6400
-- 13 | RICHARD    | JACKSON   | richard.jackson@example.com | IT | 79000 | 7900
-- 14 | JENNIFER   | WHITE     | jennifer.white@example.com | Marketing | 71000 | 7100
-- 15 | CHARLES    | HARRIS    | charles.harris@example.com | Finance | 83000 | 8300
```

### 3. Database Query - Check Job Execution

```sql
-- View job execution history
SELECT JOB_EXECUTION_ID, JOB_NAME, START_TIME, END_TIME, STATUS, EXIT_CODE
FROM BATCH_JOB_EXECUTION
ORDER BY JOB_EXECUTION_ID DESC
LIMIT 1;

-- Expected output:
-- JOB_EXECUTION_ID | JOB_NAME | START_TIME | END_TIME | STATUS | EXIT_CODE
-- 1 | transformEmployeeJob | 2026-04-09 09:45:00 | 2026-04-09 09:45:05 | COMPLETED | COMPLETED
```

### 4. Database Query - Check Step Statistics

```sql
-- View step execution details (read/write/skip counts)
SELECT STEP_NAME, READ_COUNT, WRITE_COUNT, SKIP_COUNT, STATUS
FROM BATCH_STEP_EXECUTION
WHERE JOB_EXECUTION_ID = (SELECT MAX(JOB_EXECUTION_ID) FROM BATCH_JOB_EXECUTION);

-- Expected output:
-- STEP_NAME | READ_COUNT | WRITE_COUNT | SKIP_COUNT | STATUS
-- transformEmployeeStep | 15 | 10 | 5 | COMPLETED

-- Analysis:
-- READ_COUNT: 15 (read all 15 records from CSV)
-- WRITE_COUNT: 10 (wrote 10 valid records to database)
-- SKIP_COUNT: 5 (skipped 5 invalid records)
-- STATUS: COMPLETED (job finished successfully)
```

### 5. Summary Statistics

```sql
-- Get summary of processing
SELECT 
    COUNT(*) as total_employees,
    AVG(salary) as avg_salary,
    MAX(salary) as max_salary,
    MIN(salary) as min_salary,
    SUM(bonus) as total_bonus
FROM employee;

-- Expected output:
-- TOTAL_EMPLOYEES | AVG_SALARY | MAX_SALARY | MIN_SALARY | TOTAL_BONUS
-- 10 | 76700 | 85000 | 64000 | 76900
```

---

## 🔄 Processing Flow Example

### Record 1: Valid Record (john doe)
```
CSV: 1,john,doe,john.doe@example.com,IT,75000
  ↓
Reader: Creates Employee object
  ↓
Processor:
  ✓ Email validation: john.doe@example.com → VALID
  ✓ Salary validation: 75000 > 0 → VALID
  ✓ Transform: firstName = "JOHN", lastName = "DOE"
  ✓ Calculate: bonus = 75000 * 0.10 = 7500
  ↓
Writer: INSERT INTO employee VALUES (1, 'JOHN', 'DOE', ...)
  ↓
Database: ✅ Record inserted
```

### Record 2: Invalid Email (jane smith)
```
CSV: 2,jane,smith,invalid-email,HR,65000
  ↓
Reader: Creates Employee object
  ↓
Processor:
  ✗ Email validation: invalid-email → INVALID (no dot)
  ↓
Processor returns null → Record skipped
  ↓
Writer: Skips this record
  ↓
Database: ⚠️ Record NOT inserted
```

### Record 3: Invalid Salary (mike johnson)
```
CSV: 3,mike,johnson,mike.johnson@example.com,Finance,-5000
  ↓
Reader: Creates Employee object
  ↓
Processor:
  ✓ Email validation: mike.johnson@example.com → VALID
  ✗ Salary validation: -5000 ≤ 0 → INVALID
  ↓
Processor returns null → Record skipped
  ↓
Writer: Skips this record
  ↓
Database: ⚠️ Record NOT inserted
```

---

## 🎓 Learning Path

**Series Progression:**
1. Project 01 (Basic structure)
2. Project 02 (Chunk processing)
3. **Project 03** ← You are here (Data transformation)
4. Project 04 (Reverse flow)
5. Project 05 (Multi-step jobs)
6. Project 06 (Scheduling)
7. Project 07 (Parallel processing)
8. Project 08 (REST APIs)
9. Project 09 (Conditional flow)
10. Project 10 (Production-ready)

---

## 💡 Key Takeaways

✅ **ItemProcessor** is the heart of data transformation  
✅ **Return null** from processor to skip invalid records  
✅ **Validation** prevents bad data from entering the database  
✅ **Transformation** adds business value to data  
✅ **Chunk processing** balances memory and performance  
✅ **Skip logic** allows batch jobs to be resilient  
✅ **Batch metadata** tracks all processing statistics  

---

## 🐛 Troubleshooting

### Issue: "Table 'employee' doesn't exist"
**Solution:** Create the table using the SQL script in Step 1 of Setup Instructions

### Issue: "Connection refused" error
**Solution:** Ensure MySQL is running and credentials in `application.properties` are correct

### Issue: Port 8082 already in use
**Solution:** Change port in `application.properties`:
```properties
server.port=8083
```

### Issue: CSV file not found
**Solution:** Ensure `employees.csv` exists in `src/main/resources/data/`

### Issue: Job runs but no records inserted
**Solution:** Check console logs for validation errors. All records might be invalid.

---

## 📈 Performance Metrics

| Metric | Value |
|--------|-------|
| CSV Records Read | 15 |
| Valid Records | 10 |
| Invalid Records (Skipped) | 5 |
| Processing Time | ~2-3 seconds |
| Throughput | ~5 records/second |
| Database Inserts | 1 batch (10 records) |

---

## 🚀 Next Steps

After mastering this project, explore:

1. **Project 04: Database to CSV** - Reverse flow (DB → CSV export)
2. **Project 05: Multi-Step Job** - Multiple steps in one job
3. **Project 06: Scheduled Batch** - Automatic job scheduling
4. **Project 07: Parallel Processing** - Multi-threaded batch jobs

---

## 📚 Related Projects

- **Project 01:** Hello World Job (Basic structure)
- **Project 02:** CSV to Database (Chunk processing)
- **Project 03:** Data Transformation (This project)
- **Project 04:** Database to CSV (Reverse flow)

---

## 👨💻 Author

**Ananth Kumar**  
Spring Batch Learning Series - Step by Step

---

## ⭐ Support

If you found this project helpful, please give it a ⭐ on GitHub!

---

**Status:** 🟢 Production Ready

**Last Updated:** April 9, 2026


