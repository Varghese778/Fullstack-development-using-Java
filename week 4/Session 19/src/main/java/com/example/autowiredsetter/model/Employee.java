package com.example.autowiredsetter.model;

public class Employee {
    private int id;
    private String name;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Employee [ID=" + id + ", Name=" + name + "]";
    }
}