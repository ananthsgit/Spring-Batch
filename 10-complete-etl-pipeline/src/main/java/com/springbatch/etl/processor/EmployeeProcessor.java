package com.springbatch.etl.processor;

import com.springbatch.etl.model.Employee;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

    @Override
    public Employee process(Employee employee) throws Exception {
        // Validation
        if (employee.getEmail() == null || !employee.getEmail().contains("@")) {
            System.out.println("⚠️  Skipping invalid email: " + employee.getEmail());
            return null;
        }

        if (employee.getSalary() == null || employee.getSalary() <= 0) {
            System.out.println("⚠️  Skipping invalid salary: " + employee.getSalary());
            return null;
        }

        // Transformation
        employee.setFirstName(employee.getFirstName().toUpperCase());
        employee.setLastName(employee.getLastName().toUpperCase());

        return employee;
    }
}
