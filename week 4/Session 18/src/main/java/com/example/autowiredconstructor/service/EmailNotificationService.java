package com.example.autowiredconstructor.service;

import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailNotificationService implements NotificationService {
    @Override
    public String sendNotification() {
        return "Task 4.4: Email Notification Sent!";
    }
}