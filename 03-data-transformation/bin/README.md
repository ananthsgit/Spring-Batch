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
