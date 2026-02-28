package com.example.autowiredconstructor.service;

import org.springframework.stereotype.Service;

@Service("smsService")
public class SMSNotificationService implements NotificationService {
    @Override
    public String sendNotification() {
        return "Task 4.4: SMS Notification Sent!";
    }
}