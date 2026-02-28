package com.example.autowiredsetter;

import com.example.autowiredsetter.model.Employee;
import com.example.autowiredsetter.service.EmployeeService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutowiredsetterApplication {

    public static void main(String[] args) {
        // Start application and get the ApplicationContext (which implements BeanFactory)
        BeanFactory factory = SpringApplication.run(AutowiredsetterApplication.class, args);

        // Syllabus requirement: Use BeanFactory to manage beans
        EmployeeService employeeService = factory.getBean(EmployeeService.class);

        // Store employee data in memory
        employeeService.registerEmployee(new Employee(101, "Alice Smith"));
        employeeService.registerEmployee(new Employee(102, "Bob Johnson"));

        // Output to console to verify Task 4.6 is complete
        System.out.println("\n--- Task 4.6: Employee Management Module ---");
        System.out.println("Employees in Memory: " + employeeService.getEmployees());
        System.out.println("--------------------------------------------\n");
    }
}