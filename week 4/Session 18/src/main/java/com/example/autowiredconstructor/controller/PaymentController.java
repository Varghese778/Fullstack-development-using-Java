package com.example.autowiredconstructor.controller;

import com.example.autowiredconstructor.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    
    private final PaymentService paymentService;

    // Syllabus specifically asks to use @Autowired on the constructor
    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/pay")
    public String pay() {
        return paymentService.processPayment();
    }
}