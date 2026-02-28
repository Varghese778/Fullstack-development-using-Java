package com.example.autowiredconstructor.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public String processPayment() {
        return "Task 4.3: Payment processed successfully using Constructor Injection!";
    }
}