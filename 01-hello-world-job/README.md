# 📦 Project 01: Hello World Job

**Difficulty:** ⭐ Beginner

---

## 🎯 What This Project Does

A simple Spring Batch job that prints "Hello World" to the console when triggered via REST API.

**Purpose:** Learn the basic structure of Spring Batch (Job → Step → Tasklet)

---

## 🏗️ Architecture

```
REST API (/api/batch/hello)
    ↓
JobLauncher
    ↓
Job (helloWorldJob)
    ↓
Step (helloWorldStep)
    ↓
Tasklet (HelloWorldTasklet)
    ↓
Console Output
```

---

## 📚 Key Concepts Learned

✅ **Job** - Container for the entire batch process  
✅ **Step** - Individual unit of work  
✅ **Tasklet** - Simple task execution  
✅ **JobLauncher** - Component that starts jobs  
✅ **JobRepository** - Stores job execution metadata  
✅ **JobParameters** - Makes each job run unique  

---

## 📂 Project Structure

```
01-hello-world-job/
├── pom.xml
├── src/main/java/com/springbatch/hello/
│   ├── HelloWorldJobApplication.java    # Main Spring Boot app
│   ├── config/
│   │   └── BatchConfig.java             # Job & Step configuration
│   ├── tasklet/
│   │   └── HelloWorldTasklet.java       # Custom logic
│   └── controller/
│       └── BatchController.java         # REST endpoint
└── src/main/resources/
    └── application.properties           # Database config
```

---

## 🔧 Technologies Used

- **Spring Boot:** 3.2.0
- **Spring Batch:** 5.x
- **MySQL:** 8.x
- **Java:** 17
- **Maven:** Build tool

---

## ⚙️ Setup Instructions

### 1. Database Setup

```sql
CREATE DATABASE spring_batch_db;
```

### 2. Update application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring_batch_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Import Project in Spring Tool Suite

- File → Import → Existing Maven Projects
- Browse to: `01-hello-world-job`
- Click Finish

### 4. Run Application

- Right-click `HelloWorldJobApplication.java`
- Run As → Spring Boot App
- Wait for: `Started HelloWorldJobApplication in X seconds`

---

## 🧪 Testing

### Trigger the Job

**Using Postman:**
```
Method: POST
URL: http://localhost:8080/api/batch/hello
```

**Using Browser Console:**
```javascript
fetch('http://localhost:8080/api/batch/hello', {method: 'POST'})
  .then(r => r.text())
  .then(console.log)
```

**Using PowerShell:**
```powershell
Invoke-WebRequest -Uri http://localhost:8080/api/batch/hello -Method POST
```

---

## 📊 Expected Output

### Console Output:
```
========================================
🎉 Hello from Spring Batch!
📦 Job Name: helloWorldJob
📌 Step Name: helloWorldStep
========================================
```

### API Response:
```
✅ Hello World Job executed successfully!
```

### Database Tables Created:

Spring Batch automatically creates these metadata tables:

```
BATCH_JOB_INSTANCE
BATCH_JOB_EXECUTION
BATCH_JOB_EXECUTION_PARAMS
BATCH_JOB_EXECUTION_CONTEXT
BATCH_STEP_EXECUTION
BATCH_STEP_EXECUTION_CONTEXT
BATCH_JOB_SEQ
BATCH_STEP_EXECUTION_SEQ
BATCH_JOB_EXECUTION_SEQ
```

---

## 🔍 Verify Job Execution

### Check Job Execution History:

```sql
USE spring_batch_db;

SELECT JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, EXIT_CODE 
FROM BATCH_JOB_EXECUTION;
```

**Expected Result:**
```
JOB_EXECUTION_ID | START_TIME          | END_TIME            | STATUS    | EXIT_CODE
1                | 2026-04-07 11:06:20 | 2026-04-07 11:06:21 | COMPLETED | COMPLETED
```

---

## 🔄 What Happens Internally

### Step-by-Step Flow:

1. **REST Request Received**
   - Endpoint: `/api/batch/hello`
   - Controller: `BatchController.runHelloWorldJob()`

2. **JobParameters Created**
   ```java
   JobParameters params = new JobParametersBuilder()
       .addLong("time", System.currentTimeMillis())
       .toJobParameters();
   ```
   - Timestamp makes each run unique

3. **JobLauncher Starts Job**
   ```java
   jobLauncher.run(helloWorldJob, params);
   ```

4. **JobRepository Checks**
   - "Has this job with these parameters run before?"
   - Creates new record in `BATCH_JOB_INSTANCE`

5. **Job Execution Starts**
   - Record created in `BATCH_JOB_EXECUTION`
   - Status: STARTED

6. **Step Execution Starts**
   - Step: `helloWorldStep`
   - Calls: `HelloWorldTasklet.execute()`

7. **Tasklet Executes**
   ```java
   System.out.println("🎉 Hello from Spring Batch!");
   return RepeatStatus.FINISHED;
   ```

8. **Step Completes**
   - Updates `BATCH_STEP_EXECUTION`
   - Status: COMPLETED

9. **Job Completes**
   - Updates `BATCH_JOB_EXECUTION`
   - Status: COMPLETED
   - Exit Code: COMPLETED

10. **Response Returned**
    ```
    ✅ Hello World Job executed successfully!
    ```

---

## 💡 Key Takeaways

✅ A Job contains one or more Steps  
✅ Tasklet is for simple, single-task operations  
✅ JobParameters make each execution unique  
✅ Spring Batch tracks all executions in the database  
✅ Jobs can be triggered via REST APIs  

---

## 🐛 Troubleshooting

### Issue: Database connection error
**Solution:** Check MySQL is running and credentials in `application.properties` are correct

### Issue: Port 8080 already in use
**Solution:** Change port in `application.properties`:
```properties
server.port=8081
```

### Issue: Job runs automatically on startup
**Solution:** Ensure this property is set:
```properties
spring.batch.job.enabled=false
```

---

## 🚀 Next Steps

Move to **Project 02: CSV to Database** to learn:
- ItemReader (read from CSV)
- ItemWriter (write to database)
- Chunk-oriented processing

---

## 👨‍💻 Author

**Ananth Kumar**  
Learning Spring Batch - Step by Step

---

## 📅 Project Completed

Date: April 7, 2026  
Status: ✅ Working

---

### API Response:
```
✅ Hello World Job executed successfully!
```

### Console Output:
```
========================================
🎉 Hello from Spring Batch!
📦 Job Name: helloWorldJob
📌 Step Name: helloWorldStep
========================================
```

### Database Verification:
```sql
mysql> SELECT * FROM BATCH_JOB_EXECUTION;

JOB_EXECUTION_ID: 1
START_TIME: 2026-04-07 11:06:20.853366
END_TIME: 2026-04-07 11:06:21.201514
STATUS: COMPLETED
EXIT_CODE: COMPLETED
```

---

**Status:** 🟢 Production Ready
