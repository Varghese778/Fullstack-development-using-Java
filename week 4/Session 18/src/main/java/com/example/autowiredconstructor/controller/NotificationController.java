package com.example.autowiredconstructor.controller;

import com.example.autowiredconstructor.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    @Qualifier("emailService") // This tells Spring exactly which one to use
    private NotificationService notificationService;

    @GetMapping("/notify")
    public String notifyUser() {
        return notificationService.sendNotification();
    }
}