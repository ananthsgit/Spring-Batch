package com.springbatch.multistep.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CalculateStatsTasklet implements Tasklet {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static Map<String, Object> sharedStats = new HashMap<>();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("\n========================================");
        System.out.println("📊 Step 2: Calculating Statistics");
        System.out.println("========================================\n");

        try {
            String countSql = "SELECT COUNT(*) FROM employee";
            Integer totalCount = jdbcTemplate.queryForObject(countSql, Integer.class);

            String avgSalarySql = "SELECT AVG(salary) FROM employee";
            Double avgSalary = jdbcTemplate.queryForObject(avgSalarySql, Double.class);

            String maxSalarySql = "SELECT MAX(salary) FROM employee";
            Double maxSalary = jdbcTemplate.queryForObject(maxSalarySql, Double.class);

            String minSalarySql = "SELECT MIN(salary) FROM employee";
            Double minSalary = jdbcTemplate.queryForObject(minSalarySql, Double.class);

            sharedStats.put("totalCount", totalCount);
            sharedStats.put("avgSalary", avgSalary);
            sharedStats.put("maxSalary", maxSalary);
            sharedStats.put("minSalary", minSalary);

            System.out.println("✅ Statistics Calculated:");
            System.out.println("   Total Employees: " + totalCount);
            System.out.println("   Average Salary: $" + String.format("%.2f", avgSalary));
            System.out.println("   Maximum Salary: $" + String.format("%.2f", maxSalary));
            System.out.println("   Minimum Salary: $" + String.format("%.2f", minSalary));
            System.out.println("\n========================================\n");

            return RepeatStatus.FINISHED;
        } catch (Exception e) {
            System.out.println("❌ Error calculating statistics: " + e.getMessage());
            throw e;
        }
    }
}
