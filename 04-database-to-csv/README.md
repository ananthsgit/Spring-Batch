# 📦 Project 04: Database to CSV Export

**Difficulty:** ⭐⭐⭐ Intermediate-Advanced

A Spring Batch application that reads employee data from MySQL database and exports it to a CSV file using cursor-based reading and chunk-oriented processing.

---

## 🎯 What This Project Does

This project demonstrates **reverse ETL (Extract, Transform, Load)** - exporting data from database to files:

- ✅ Reads employee records from MySQL database
- ✅ Uses cursor-based reading (memory efficient)
- ✅ Processes data in chunks (10 records at a time)
- ✅ Exports to CSV file with proper formatting
- ✅ Tracks processing statistics (read/write counts)
- ✅ Generates reports for stakeholders

---

## 📚 Key Concepts Learned

| Concept | Description |
|---------|-------------|
| **JdbcCursorItemReader** | Reads data from database using SQL cursor |
| **FlatFileItemWriter** | Writes data to flat files (CSV, TXT) |
| **RowMapper** | Maps database ResultSet to Java objects |
| **FieldExtractor** | Extracts fields from objects for CSV output |
| **Cursor-Based Reading** | Memory efficient - doesn't load all records at once |
| **Reverse ETL** | Export data from DB to files (opposite of Project 02) |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                  DATABASE TO CSV FLOW                   │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  MySQL Database (employee table)                        │
│       ↓                                                  │
│  JdbcCursorItemReader (reads via SQL cursor)            │
│       ↓                                                  │
│  RowMapper (ResultSet → Employee object)                │
│       ↓                                                  │
│  Chunk (collects 10 Employee objects)                   │
│       ↓                                                  │
│  FlatFileItemWriter (formats as CSV)                    │
│       ↓                                                  │
│  CSV File (employees_export.csv)                        │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## 📂 Project Structure

```
04-database-to-csv/
├── src/main/java/com/springbatch/dbtocsv/
│   ├── DbToCsvApplication.java          # Main Spring Boot app
│   ├── config/
│   │   └── BatchConfig.java             # Reader, Writer, Job config
│   ├── model/
│   │   └── Employee.java                # JPA Entity
│   ├── mapper/
│   │   └── EmployeeRowMapper.java       # Maps DB row → Object
│   └── controller/
│       └── BatchController.java         # REST endpoint
├── src/main/resources/
│   ├── application.properties           # Database & server config
│   └── output/                          # Generated CSV files
├── pom.xml                              # Maven dependencies
└── README.md                            # This file
```

---

## 🔧 Technologies Used

| Technology | Version | Purpose |
|-----------|---------|---------|
| Spring Boot | 3.2.0 | Application framework |
| Spring Batch | 5.x | Batch processing |
| Spring Data JPA | Latest | ORM & database access |
| MySQL | 8.x | Database |
| Java | 17+ | Programming language |
| Maven | Latest | Build tool |

---

## ⚙️ Setup Instructions

### Prerequisites
- Java 17 or higher
- MySQL 8.x
- Maven 3.6+
- Spring Tool Suite or any IDE
- Project 02 data (15 employees in database)

### Step 1: Verify Database Has Data

Ensure you have employee data from Project 02:

```sql
USE spring_batch_db;
SELECT COUNT(*) FROM employee;
-- Should return: 15
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
server.port=8083
```

### Step 3: Build & Run

**Option A: Using Maven**
```bash
cd 04-database-to-csv
mvn clean install
mvn spring-boot:run
```

**Option B: Using Spring Tool Suite**
1. Right-click `DbToCsvApplication.java`
2. Select **Run As** → **Spring Boot App**
3. Wait for: `Started DbToCsvApplication in X seconds`

---

## 🧪 Testing & Triggering the Job

### Trigger the Export Job

**Using PowerShell (Windows):**
```powershell
Invoke-WebRequest -Uri http://localhost:8083/api/batch/export-csv -Method POST
```

**Using curl (Linux/Mac):**
```bash
curl -X POST http://localhost:8083/api/batch/export-csv
```

**Using Postman:**
- Method: `POST`
- URL: `http://localhost:8083/api/batch/export-csv`
- Click Send

**Expected Response:**
```
✅ Database to CSV Export Job executed successfully! Check output/employees_export.csv
```

---

## 📊 Checking Output

### 1. Console Output

Watch the application console for processing logs:

```
Executing step: [exportEmployeeStep]
Reading from database...
Processing records...
Chunk 1: Read 10 records
Chunk 1: Written 10 records
Chunk 2: Read 5 records
Chunk 2: Written 5 records
Step: [exportEmployeeStep] executed in XXXms
Job: [exportEmployeeJob] completed with status: [COMPLETED]
```

### 2. Check Generated CSV File

**Location:** `src/main/resources/output/employees_export.csv`

**Expected Content:**
```csv
id,firstName,lastName,email,department,salary
1,John,Doe,john.doe@example.com,IT,75000.0
2,Jane,Smith,jane.smith@example.com,HR,65000.0
3,Mike,Johnson,mike.johnson@example.com,Finance,80000.0
4,Sarah,Williams,sarah.williams@example.com,IT,80000.0
5,David,Brown,david.brown@example.com,Marketing,68000.0
6,Emily,Davis,emily.davis@example.com,IT,72000.0
7,James,Miller,james.miller@example.com,HR,62000.0
8,Linda,Wilson,linda.wilson@example.com,Finance,85000.0
9,Robert,Moore,robert.moore@example.com,IT,76000.0
10,Maria,Taylor,maria.taylor@example.com,Marketing,70000.0
11,William,Anderson,william.anderson@example.com,Finance,82000.0
12,Patricia,Thomas,patricia.thomas@example.com,HR,64000.0
13,Richard,Jackson,richard.jackson@example.com,IT,79000.0
14,Jennifer,White,jennifer.white@example.com,Marketing,71000.0
15,Charles,Harris,charles.harris@example.com,Finance,83000.0
```

### 3. Database Query - Check Job Execution

```sql
SELECT JOB_EXECUTION_ID, JOB_NAME, START_TIME, END_TIME, STATUS, EXIT_CODE
FROM BATCH_JOB_EXECUTION
ORDER BY JOB_EXECUTION_ID DESC
LIMIT 1;

-- Expected output:
-- JOB_EXECUTION_ID | JOB_NAME | START_TIME | END_TIME | STATUS | EXIT_CODE
-- 3 | exportEmployeeJob | 2026-04-10 14:30:00 | 2026-04-10 14:30:05 | COMPLETED | COMPLETED
```

### 4. Database Query - Check Step Statistics

```sql
SELECT STEP_NAME, READ_COUNT, WRITE_COUNT, COMMIT_COUNT, STATUS
FROM BATCH_STEP_EXECUTION
WHERE JOB_EXECUTION_ID = (SELECT MAX(JOB_EXECUTION_ID) FROM BATCH_JOB_EXECUTION);

-- Expected output:
-- STEP_NAME | READ_COUNT | WRITE_COUNT | COMMIT_COUNT | STATUS
-- exportEmployeeStep | 15 | 15 | 2 | COMPLETED

-- Analysis:
-- READ_COUNT: 15 (read all 15 records from database)
-- WRITE_COUNT: 15 (wrote 15 records to CSV file)
-- COMMIT_COUNT: 2 (2 chunks processed: 10 + 5)
-- STATUS: COMPLETED (job finished successfully)
```

---

## 🔄 Processing Flow Example

### Detailed Internal Flow

**1. Job Starts**
```
JobLauncher triggers exportEmployeeJob
JobParameters: {startTime: 1712748600000}
```

**2. Step Starts**
```
Step: exportEmployeeStep
Chunk size: 10
```

**3. Chunk 1 Processing (Records 1-10)**

**Reading Phase:**
```
1. JdbcCursorItemReader opens database cursor
2. Executes SQL: SELECT id, first_name, last_name, email, department, salary FROM employee
3. Cursor positioned at first row
4. RowMapper converts each row:
   - Row 1: ResultSet → Employee(id=1, firstName="John", lastName="Doe", ...)
   - Row 2: ResultSet → Employee(id=2, firstName="Jane", lastName="Smith", ...)
   ...
   - Row 10: ResultSet → Employee(id=10, firstName="Maria", lastName="Taylor", ...)
5. Chunk buffer: List<Employee> with 10 objects
```

**Writing Phase:**
```
1. FlatFileItemWriter receives List<Employee> (10 items)
2. FieldExtractor extracts fields from each Employee:
   - Employee(1, John, Doe, ...) → "1,John,Doe,john.doe@example.com,IT,75000.0"
   - Employee(2, Jane, Smith, ...) → "2,Jane,Smith,jane.smith@example.com,HR,65000.0"
   ...
   - Employee(10, Maria, Taylor, ...) → "10,Maria,Taylor,maria.taylor@example.com,Marketing,70000.0"
3. Writes to file:
   id,firstName,lastName,email,department,salary
   1,John,Doe,john.doe@example.com,IT,75000.0
   2,Jane,Smith,jane.smith@example.com,HR,65000.0
   ...
   10,Maria,Taylor,maria.taylor@example.com,Marketing,70000.0
4. Transaction commits ✅
5. Chunk buffer cleared
```

**4. Chunk 2 Processing (Records 11-15)**

**Reading Phase:**
```
1. Cursor continues from row 11
2. RowMapper converts rows 11-15:
   - Row 11: ResultSet → Employee(id=11, firstName="William", ...)
   - Row 12: ResultSet → Employee(id=12, firstName="Patricia", ...)
   ...
   - Row 15: ResultSet → Employee(id=15, firstName="Charles", ...)
3. Chunk buffer: List<Employee> with 5 objects (less than chunk size, OK!)
```

**Writing Phase:**
```
1. FlatFileItemWriter receives List<Employee> (5 items)
2. FieldExtractor extracts fields:
   - Employee(11, William, Anderson, ...) → "11,William,Anderson,william.anderson@example.com,Finance,82000.0"
   ...
   - Employee(15, Charles, Harris, ...) → "15,Charles,Harris,charles.harris@example.com,Finance,83000.0"
3. Appends to file:
   11,William,Anderson,william.anderson@example.com,Finance,82000.0
   12,Patricia,Thomas,patricia.thomas@example.com,HR,64000.0
   13,Richard,Jackson,richard.jackson@example.com,IT,79000.0
   14,Jennifer,White,jennifer.white@example.com,Marketing,71000.0
   15,Charles,Harris,charles.harris@example.com,Finance,83000.0
4. Transaction commits ✅
5. Cursor returns null (EOF - End of File)
```

**5. Step Completes**
```
Updates BATCH_STEP_EXECUTION
Status: COMPLETED
READ_COUNT: 15
WRITE_COUNT: 15
COMMIT_COUNT: 2
```

**6. Job Completes**
```
Updates BATCH_JOB_EXECUTION
Status: COMPLETED
Exit Code: COMPLETED
Duration: ~5 seconds
```

---

## 🎓 Learning Path

**Series Progression:**
1. Project 01 (Basic structure)
2. Project 02 (Chunk processing)
3. Project 03 (Data transformation)
4. **Project 04** ← You are here (Reverse flow)
5. Project 05 (Multi-step jobs)
6. Project 06 (Scheduling)
7. Project 07 (Parallel processing)
8. Project 08 (REST APIs)
9. Project 09 (Conditional flow)
10. Project 10 (Production-ready)

---

## 💡 Key Takeaways

✅ **JdbcCursorItemReader** reads from database efficiently using cursors
✅ **RowMapper** converts database rows to Java objects
✅ **FlatFileItemWriter** formats and writes data to CSV files
✅ **Chunk processing** balances memory and performance
✅ **Reverse ETL** exports data from DB to files
✅ **Cursor-based reading** is memory efficient for large datasets
✅ **Batch metadata** tracks all processing statistics

---

## 🎯 Comparison: Project 02 vs Project 04

| Aspect | Project 02 (CSV → DB) | Project 04 (DB → CSV) |
|--------|----------------------|----------------------|
| **Reader** | FlatFileItemReader | JdbcCursorItemReader |
| **Writer** | JdbcBatchItemWriter | FlatFileItemWriter |
| **Source** | CSV file | MySQL database |
| **Destination** | MySQL database | CSV file |
| **Mapper** | LineMapper | RowMapper |
| **Use Case** | Data Import | Data Export |
| **Flow Direction** | Extract → Load | Extract → Load |
| **Chunk Size** | 10 | 10 |
| **Records Processed** | 15 | 15 |

---

## 🐛 Troubleshooting

### Issue: "Table 'employee' doesn't exist"
**Solution:** Run Project 02 first to populate the employee table with data

### Issue: "Connection refused" error
**Solution:** Ensure MySQL is running and credentials in `application.properties` are correct

### Issue: Port 8083 already in use
**Solution:** Change port in `application.properties`:
```properties
server.port=8084
```

### Issue: CSV file not created
**Solution:** Ensure `src/main/resources/output/` directory exists and is writable

### Issue: Job runs but CSV is empty
**Solution:** Verify employee table has data:
```sql
SELECT COUNT(*) FROM employee;
```

---

## 📈 Performance Metrics

| Metric | Value |
|--------|-------|
| Database Records Read | 15 |
| CSV Records Written | 15 |
| Processing Time | ~2-3 seconds |
| Throughput | ~5 records/second |
| Chunks Processed | 2 |
| Transactions Committed | 2 |
| File Size | ~500 bytes |

---

## 🚀 Next Steps

After mastering this project, explore:

1. **Project 05: Multi-Step Job** - Multiple steps in one job
2. **Project 06: Scheduled Batch** - Automatic job scheduling
3. **Project 07: Parallel Processing** - Multi-threaded batch jobs
4. **Project 08: REST API Batch** - Advanced REST integration

---

## 📚 Related Projects

- **Project 01:** Hello World Job (Basic structure)
- **Project 02:** CSV to Database (Data import)
- **Project 03:** Data Transformation (Validation & processing)
- **Project 04:** Database to CSV (This project - Data export)

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
