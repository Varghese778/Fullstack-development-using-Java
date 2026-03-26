package com.example.accountmng_ms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @GetMapping("/account/status")
    public String getStatus() {
        return "Account Management Microservice is running successfully.";
    }
}