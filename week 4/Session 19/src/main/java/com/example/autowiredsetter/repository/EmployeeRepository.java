package com.example.autowiredsetter.repository;

import com.example.autowiredsetter.model.Employee;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component // Syllabus requirement: Use @Component
public class EmployeeRepository {
    
    // Syllabus requirement: Store employee data in memory
    private List<Employee> employeeDatabase = new ArrayList<>();

    public void addEmployee(Employee emp) {
        employeeDatabase.add(emp);
    }

    public List<Employee> getAllEmployees() {
        return employeeDatabase;
    }
}