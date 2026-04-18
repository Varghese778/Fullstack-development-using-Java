package com.jobportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Job Portal Management System.
 * Connects job seekers and employers with features for profile management,
 * resume uploads, job listings, applications, and employer dashboards.
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class JobPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobPortalApplication.class, args);
    }
}
