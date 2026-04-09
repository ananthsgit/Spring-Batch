package com.springbatch.multistep.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class GenerateReportTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("\n========================================");
        System.out.println("📄 Step 3: Generating Summary Report");
        System.out.println("========================================\n");

        try {
            Map<String, Object> stats = CalculateStatsTasklet.sharedStats;

            if (stats == null || stats.isEmpty()) {
                throw new RuntimeException("Statistics not found!");
            }

            Integer totalCount = (Integer) stats.get("totalCount");
            Double avgSalary = (Double) stats.get("avgSalary");
            Double maxSalary = (Double) stats.get("maxSalary");
            Double minSalary = (Double) stats.get("minSalary");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String processedDate = now.format(formatter);

            StringBuilder report = new StringBuilder();
            report.append("========================================\n");
            report.append("Employee Processing Summary\n");
            report.append("========================================\n");
            report.append("\n");
            report.append("Total Employees: ").append(totalCount).append("\n");
            report.append("Average Salary: $").append(String.format("%.2f", avgSalary)).append("\n");
            report.append("Highest Salary: $").append(String.format("%.2f", maxSalary)).append("\n");
            report.append("Lowest Salary: $").append(String.format("%.2f", minSalary)).append("\n");
            report.append("Processed Date: ").append(processedDate).append("\n");
            report.append("\n");
            report.append("========================================\n");

            String filePath = "src/main/resources/output/summary_report.txt";
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(report.toString());
            }

            System.out.println("✅ Report Generated Successfully!");
            System.out.println("📁 File Location: " + filePath);
            System.out.println("\n" + report.toString());
            System.out.println("========================================\n");

            return RepeatStatus.FINISHED;
        } catch (IOException e) {
            System.out.println("❌ Error writing report file: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("❌ Error generating report: " + e.getMessage());
            throw e;
        }
    }
}
