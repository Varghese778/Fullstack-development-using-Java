package com.example.user_ms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    // We inject the server port here to prove Load Balancing works later
    @Value("${server.port}")
    private String port;

    @GetMapping("/user/status")
    public String getStatus() {
        return "User Microservice is running on port: " + port;
    }
}