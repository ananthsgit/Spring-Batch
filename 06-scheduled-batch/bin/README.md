# 📦 Project 06: Scheduled Batch Job

**Difficulty:** ⭐⭐⭐ Advanced

---

## 🎯 Learning Objectives

- Schedule batch jobs automatically
- Use Spring Scheduler with Batch
- Configure cron expressions
- Prevent duplicate job executions
- Use JobParameters for unique runs

---

## 📚 Key Concepts

### 1. **@Scheduled**
Spring annotation to run methods at fixed intervals.

### 2. **Cron Expressions**
Define complex schedules (daily, weekly, monthly).

### 3. **JobParameters**
Make each job execution unique (timestamp, run ID).

### 4. **@EnableScheduling**
Enable Spring's scheduling capability.

---

## 🏗️ Architecture

```
Spring Scheduler → JobLauncher → Job → Steps → Database
     ↓
  Cron Trigger (e.g., daily at midnight)
```

---

## 📂 Project Structure

```
06-scheduled-batch/
├── src/main/java/com/springbatch/scheduled/
│   ├── config/
│   │   └── BatchConfig.java
│   ├── scheduler/
│   │   └── BatchScheduler.java       # Scheduled job trigger
│   ├── model/
│   │   └── Employee.java
│   ├── controller/
│   │   └── BatchController.java      # Manual trigger (optional)
│   └── ScheduledApplication.java
├── src/main/resources/
│   ├── application.properties
│   └── data/
│       └── employees.csv
└── pom.xml
```

---

## ⏰ Cron Expression Examples

| Expression | Meaning |
|-----------|---------|
| `0 0 0 * * ?` | Every day at midnight |
| `0 0 9 * * MON-FRI` | Weekdays at 9 AM |
| `0 0 */6 * * ?` | Every 6 hours |
| `0 0 0 1 * ?` | First day of every month |
| `0 */5 * * * ?` | Every 5 minutes |

---

## ⚙️ Setup Instructions

### 1. Enable Scheduling in Application
```java
@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
public class ScheduledApplication {
    // ...
}
```

### 2. Configure Schedule
```properties
# application.properties
batch.schedule.cron=0 0 0 * * ?  # Daily at midnight
```

### 3. Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

---

## 🧪 Testing

### Automatic Execution:
- Job runs automatically based on cron schedule
- Check logs for execution confirmation

### Manual Trigger (for testing):
```bash
POST http://localhost:8080/api/batch/trigger-now
```

### View Execution History:
```sql
SELECT job_instance_id, start_time, end_time, status 
FROM BATCH_JOB_EXECUTION 
ORDER BY start_time DESC;
```

---

## 🔍 What Happens Internally?

1. **Scheduler Triggers:**
   - Cron expression matches current time
   - BatchScheduler method executes

2. **JobParameters Created:**
   - Timestamp added (makes run unique)
   - Optional: run ID, file name, etc.

3. **Job Launches:**
   - JobLauncher starts the job
   - Job executes all steps

4. **Completion:**
   - Job status saved to database
   - Next execution scheduled

---

## 📊 Sample Scheduler Code

```java
@Scheduled(cron = "${batch.schedule.cron}")
public void runDailyImport() {
    JobParameters params = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();
    
    jobLauncher.run(importJob, params);
}
```

---

## 💡 Key Takeaways

✅ @Scheduled automates job execution  
✅ Cron expressions provide flexible scheduling  
✅ JobParameters prevent duplicate runs  
✅ Useful for daily/weekly reports  
✅ Combine with monitoring for production  

---

## 🚀 Next Steps

Move to **Project 07: Parallel Processing** to learn:
- Multi-threaded steps
- Partitioning
- Performance optimization

---

**Status:** 🟢 Ready for implementation
