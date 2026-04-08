package com.springbatch.transform.processor;

import com.springbatch.transform.model.Employee;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

    @Override
    public Employee process(Employee employee) throws Exception {
        System.out.println("Processing record: " + employee.getId() + " - " + 
                          employee.getFirstName() + " " + employee.getLastName());

        // Validation 1: Check email format
        if (!isValidEmail(employee.getEmail())) {
            System.out.println("⚠️ Skipped - Invalid email: " + employee.getEmail());
            return null; // Skip this record
        }

        // Validation 2: Check salary is positive
        if (employee.getSalary() == null || employee.getSalary() <= 0) {
            System.out.println("⚠️ Skipped - Invalid salary: " + employee.getSalary());
            return null; // Skip this record
        }

        // Transformation 1: Convert names to uppercase
        employee.setFirstName(employee.getFirstName().toUpperCase());
        employee.setLastName(employee.getLastName().toUpperCase());

        // Transformation 2: Calculate bonus (10% of salary)
        employee.setBonus(employee.getSalary() * 0.10);

        System.out.println("✅ Valid - Transformed to: " + employee.getFirstName() + " " + 
                          employee.getLastName() + ", Bonus: " + employee.getBonus());

        return employee; // Pass to writer
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Simple email validation: must contain @ and .
        return email.contains("@") && email.contains(".") && 
               email.indexOf("@") < email.lastIndexOf(".");
    }
}
