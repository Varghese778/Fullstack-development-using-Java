package com.example.week6_rest_api.controller;

import com.example.week6_rest_api.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // The @Valid annotation triggers the rules we set in the User model
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        // If validation passes, this code runs. If it fails, the GlobalExceptionHandler takes over!
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}