package com.example.autowiredsetter.service;

import com.example.autowiredsetter.repository.EmployeeRepository;
import com.example.autowiredsetter.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component // Syllabus requirement: Use @Component
public class EmployeeService {

    // Syllabus requirement: Demonstrate Dependency Injection using @Autowired
    @Autowired
    private EmployeeRepository employeeRepository;

    public void registerEmployee(Employee emp) {
        employeeRepository.addEmployee(emp);
    }

    public List<Employee> getEmployees() {
        return employeeRepository.getAllEmployees();
    }
}