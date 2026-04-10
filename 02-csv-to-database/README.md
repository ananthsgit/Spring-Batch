# 📦 Project 02: CSV to Database

**Difficulty:** ⭐⭐ Intermediate

---

## 🎯 What This Project Does

Imports employee data from a CSV file into MySQL database using chunk-oriented processing.

**Purpose:** Learn ItemReader, ItemWriter, and chunk-based batch processing

---

## 🏗️ Architecture

```
CSV File (employees.csv)
    ↓
FlatFileItemReader (reads line by line)
    ↓
Maps CSV columns → Employee object
    ↓
Chunk (collects 10 Employee objects)
    ↓
JdbcBatchItemWriter (batch insert to MySQL)
    ↓
Transaction commits
    ↓
Repeat until EOF
```

---

## 📚 Key Concepts Learned

✅ **ItemReader** - Read data from CSV files  
✅ **ItemWriter** - Write data to database  
✅ **Chunk-Oriented Processing** - Process data in batches  
✅ **FlatFileItemReader** - CSV file reading  
✅ **JdbcBatchItemWriter** - Efficient batch inserts  
✅ **Transaction Management** - One transaction per chunk  

---

## 📂 Project Structure

```
02-csv-to-database/
├── pom.xml
├── src/main/java/com/springbatch/csvtodb/
│   ├── CsvToDbApplication.java          # Main Spring Boot app
│   ├── model/
│   │   └── Employee.java                # JPA Entity
│   ├── config/
│   │   └── BatchConfig.java             # Reader + Writer + Job
│   └── controller/
│       └── BatchController.java         # REST endpoint
└── src/main/resources/
    ├── application.properties           # Database config
    └── data/
        └── employees.csv                # Sample CSV file (15 records)
```

---

## 🔧 Technologies Used

- **Spring Boot:** 3.2.0
- **Spring Batch:** 5.x
- **Spring Data JPA:** For entity management
- **MySQL:** 8.x
- **Java:** 17
- **Maven:** Build tool

---

## 📄 Sample CSV Format

**employees.csv:**
```csv
id,firstName,lastName,email,department,salary
1,John,Doe,john.doe@example.com,IT,75000
2,Jane,Smith,jane.smith@example.com,HR,65000
3,Mike,Johnson,mike.johnson@example.com,Finance,80000
...
```

**Total Records:** 15 employees

---

## ⚙️ Setup Instructions

### 1. Database Setup

```sql
CREATE DATABASE spring_batch_db;
```

The `employee` table will be created automatically by JPA.

**Table Structure:**
```sql
CREATE TABLE employee (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    department VARCHAR(255),
    salary DOUBLE
);
```

### 2. Update application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring_batch_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Import Project in Spring Tool Suite

- File → Import → Existing Maven Projects
- Browse to: `02-csv-to-database`
- Click Finish

### 4. Run Application

- Right-click `CsvToDbApplication.java`
- Run As → Spring Boot App
- Wait for: `Started CsvToDbApplication on port 8081`

---

## 🧪 Testing

### Trigger the Job

**Using Postman:**
```
Method: POST
URL: http://localhost:8081/api/batch/import-csv
```

**Using Browser Console:**
```javascript
fetch('http://localhost:8081/api/batch/import-csv', {method: 'POST'})
  .then(r => r.text())
  .then(console.log)
```

**Using PowerShell:**
```powershell
Invoke-WebRequest -Uri http://localhost:8081/api/batch/import-csv -Method POST
```

---

## 📊 Expected Output

### API Response:
```
✅ CSV Import Job executed successfully! Check database for records.
```

### Console Output:
```
Executing step: [importEmployeeStep]
Reading resource: [class path resource [data/employees.csv]]
Chunk 1: Read 10 records
Chunk 1: Written 10 records
Chunk 2: Read 5 records
Chunk 2: Written 5 records
Step: [importEmployeeStep] executed in 7948ms
Job: [importEmployeeJob] completed with status: [COMPLETED]
```

---

## 🔍 Verify Results

### 1. Check Employee Table

```sql
USE spring_batch_db;
SELECT * FROM employee;
```

**Expected Result (15 records):**
```
+----+------------+-----------+--------------------------------+------------+--------+
| id | first_name | last_name | email                          | department | salary |
+----+------------+-----------+--------------------------------+------------+--------+
|  1 | John       | Doe       | john.doe@example.com           | IT         |  75000 |
|  2 | Jane       | Smith     | jane.smith@example.com         | HR         |  65000 |
|  3 | Mike       | Johnson   | mike.johnson@example.com       | Finance    |  80000 |
|  4 | Sarah      | Williams  | sarah.williams@example.com     | IT         |  72000 |
|  5 | David      | Brown     | david.brown@example.com        | Marketing  |  68000 |
|  6 | Emily      | Davis     | emily.davis@example.com        | IT         |  78000 |
|  7 | James      | Miller    | james.miller@example.com       | HR         |  62000 |
|  8 | Linda      | Wilson    | linda.wilson@example.com       | Finance    |  85000 |
|  9 | Robert     | Moore     | robert.moore@example.com       | IT         |  76000 |
| 10 | Maria      | Taylor    | maria.taylor@example.com       | Marketing  |  70000 |
| 11 | William    | Anderson  | william.anderson@example.com   | Finance    |  82000 |
| 12 | Patricia   | Thomas    | patricia.thomas@example.com    | HR         |  64000 |
| 13 | Richard    | Jackson   | richard.jackson@example.com    | IT         |  79000 |
| 14 | Jennifer   | White     | jennifer.white@example.com     | Marketing  |  71000 |
| 15 | Charles    | Harris    | charles.harris@example.com     | Finance    |  83000 |
+----+------------+-----------+--------------------------------+------------+--------+
15 rows in set
```

### 2. Check Job Execution

```sql
SELECT JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, EXIT_CODE 
FROM BATCH_JOB_EXECUTION 
ORDER BY JOB_EXECUTION_ID DESC 
LIMIT 1;
```

**Expected Result:**
```
JOB_EXECUTION_ID: 2
START_TIME: 2026-04-07 16:57:28.429835
END_TIME: 2026-04-07 16:57:36.377890
STATUS: COMPLETED
EXIT_CODE: COMPLETED
Duration: ~8 seconds
```

### 3. Check Step Execution Statistics

```sql
SELECT STEP_NAME, READ_COUNT, WRITE_COUNT, COMMIT_COUNT, STATUS 
FROM BATCH_STEP_EXECUTION 
WHERE JOB_EXECUTION_ID = 2;
```

**Expected Result:**
```
STEP_NAME: importEmployeeStep
READ_COUNT: 15
WRITE_COUNT: 15
COMMIT_COUNT: 2
STATUS: COMPLETED
```

**Analysis:**
- **READ_COUNT: 15** → Read 15 records from CSV
- **WRITE_COUNT: 15** → Wrote 15 records to database
- **COMMIT_COUNT: 2** → 2 chunks processed (10 + 5)
- **STATUS: COMPLETED** → Success!

---

## 🔄 What Happens Internally

### Detailed Flow:

**1. Job Starts**
- JobLauncher triggers `importEmployeeJob`
- JobParameters: `{startTime: 1712502448429}`

**2. Step Starts**
- Step: `importEmployeeStep`
- Chunk size: 10

**3. Chunk 1 Processing (Records 1-10)**

**Reading Phase:**
```
1. FlatFileItemReader opens: employees.csv
2. Skips header line: "id,firstName,lastName,email,department,salary"
3. Reads line 1: "1,John,Doe,john.doe@example.com,IT,75000"
   → DelimitedLineTokenizer splits by comma
   → BeanWrapperFieldSetMapper maps to Employee object
   → Employee(id=1, firstName="John", lastName="Doe", email="john.doe@example.com", department="IT", salary=75000.0)
4. Repeats for lines 2-10
5. Chunk buffer: List<Employee> with 10 objects
```

**Writing Phase:**
```
1. JdbcBatchItemWriter receives List<Employee> (10 items)
2. Executes SQL:
   INSERT INTO employee (id, first_name, last_name, email, department, salary) 
   VALUES (1, 'John', 'Doe', 'john.doe@example.com', 'IT', 75000),
          (2, 'Jane', 'Smith', 'jane.smith@example.com', 'HR', 65000),
          ...
          (10, 'Maria', 'Taylor', 'maria.taylor@example.com', 'Marketing', 70000)
3. Transaction commits ✅
4. Chunk buffer cleared
```

**4. Chunk 2 Processing (Records 11-15)**

**Reading Phase:**
```
1. Reads line 11: "11,William,Anderson,william.anderson@example.com,Finance,82000"
2. Repeats for lines 12-15
3. Chunk buffer: List<Employee> with 5 objects (less than chunk size, OK!)
```

**Writing Phase:**
```
1. JdbcBatchItemWriter receives List<Employee> (5 items)
2. Executes SQL:
   INSERT INTO employee VALUES (11, ...), (12, ...), (13, ...), (14, ...), (15, ...)
3. Transaction commits ✅
4. Reader returns null (EOF - End of File)
```

**5. Step Completes**
- Updates `BATCH_STEP_EXECUTION`
- Status: COMPLETED

**6. Job Completes**
- Updates `BATCH_JOB_EXECUTION`
- Status: COMPLETED
- Total duration: ~8 seconds

---

## 🎓 Learning Path

**Series Progression:**
1. Project 01 (Basic structure)
2. **Project 02** ← You are here (Chunk processing)
3. Project 03 (Data transformation)
4. Project 04 (Reverse flow)
5. Project 05 (Multi-step jobs)
6. Project 06 (Scheduling)
7. Project 07 (Parallel processing)
8. Project 08 (REST APIs)
9. Project 09 (Conditional flow)
10. Project 10 (Production-ready)

---

## 💡 Key Takeaways

### Chunk-Oriented Processing Benefits:

**Memory Efficiency:**
```
❌ Load all 15 records → Process → Write
   Problem: OutOfMemoryError with large files

✅ Load 10 → Process → Write → Load 10 → Process → Write
   Solution: Only 10 records in memory at a time
```

**Transaction Safety:**
```
If Chunk 2 fails:
- Chunk 1 (10 records): Already committed ✅
- Chunk 2 (5 records): Rolled back ❌
- Can retry Chunk 2 without duplicating Chunk 1
```

**Performance:**
```
Single inserts: INSERT ... (1 record) × 15 = 15 DB calls
Batch inserts: INSERT ... (10 records) + INSERT ... (5 records) = 2 DB calls
Result: 7.5x faster! 🚀
```

---

## 🎯 Comparison: Tasklet vs Chunk-Oriented

| Aspect | Project 01 (Tasklet) | Project 02 (Chunk) |
|--------|---------------------|-------------------|
| **Processing Style** | Execute once | Read-Process-Write loop |
| **Data Source** | None | CSV file |
| **Data Destination** | Console | MySQL database |
| **Transaction** | 1 per step | 1 per chunk |
| **Memory Usage** | Minimal | Controlled by chunk size |
| **Use Case** | Simple tasks | Data migration/ETL |
| **Components** | Tasklet only | Reader + Writer |

---

## 🐛 Troubleshooting

### Issue: CSV file not found
**Solution:** Ensure `employees.csv` is in `src/main/resources/data/`

### Issue: Duplicate key error
**Solution:** Truncate employee table before re-running:
```sql
TRUNCATE TABLE employee;
```

### Issue: Column name mismatch
**Solution:** Check CSV header matches field names in Employee.java

### Issue: Port 8081 already in use
**Solution:** Change port in `application.properties`:
```properties
server.port=8082
```

---

## 🚀 Next Steps

Move to **Project 03: Data Transformation** to learn:
- ItemProcessor (transform data)
- Data validation
- Skip invalid records
- Business logic application

---

## 👨💻 Author

**Ananth Kumar**  
Learning Spring Batch - Step by Step

---

## 📅 Project Completed

Date: April 7, 2026  
Status: ✅ Working

---

### API Response:
```
✅ CSV Import Job executed successfully! Check database for records.
```

### Database Records:
```
15 employees imported successfully
Departments: IT (5), HR (3), Finance (4), Marketing (3)
Salary Range: $62,000 - $85,000
```

### Batch Metadata:
```
READ_COUNT: 15
WRITE_COUNT: 15
COMMIT_COUNT: 2
STATUS: COMPLETED
```

---

**Status:** 🟢 Production Ready
