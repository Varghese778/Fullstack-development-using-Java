package com.example.springmvcemployee.model;

public class Employee {
    private int id;
    private String name;
    private String department;

    public Employee(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    // Getters are required for Thymeleaf to read the data
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
}