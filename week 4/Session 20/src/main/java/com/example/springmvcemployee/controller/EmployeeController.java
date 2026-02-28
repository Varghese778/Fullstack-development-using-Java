package com.example.springmvcemployee.controller;

import com.example.springmvcemployee.model.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Notice this is @Controller, NOT @RestController!
public class EmployeeController {

    @GetMapping("/employee-details")
    public String showEmployee(Model model) {
        // 1. Create the employee details
        Employee emp = new Employee(101, "Alice Smith", "Software Engineering");
        
        // 2. Add it to the "Model" so the HTML page can access it
        model.addAttribute("employee", emp);
        
        // 3. Return the name of the HTML file (without the .html extension)
        return "employee-view";
    }
}