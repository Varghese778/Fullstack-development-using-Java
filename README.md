# Fullstack Development Using Java - Learning Repository

A comprehensive learning repository containing progressive fullstack development projects, microservices architecture implementations, and real-world applications built with **Java**, **Spring Boot**, **Node.js**, and **MySQL**.

**Author:** Sharon Varghese  
**License:** MIT  
**Last Updated:** April 2026

---

## 📚 Repository Overview

This repository documents a complete fullstack development learning journey, progressing from fundamental concepts to advanced microservices architecture. It contains:

- **2 Major Milestone Projects** - Production-ready applications
- **13 Weeks of Learning Sessions** - Progressive skill development
- **1 Assigned Task** - Practical implementation exercise
- **Microservices Architecture** - Distributed system patterns

---

## 🚀 Major Projects

### 1. **Milestone Project 1: Automated Quiz Engine with PDF Certification**

**Location:** [`Milestone_project_1/`](Milestone_project_1/)

A full-stack assessment platform for creating, managing, and distributing quizzes with AI-powered question generation and automated PDF certificate issuing.

**Tech Stack:**
- **Backend:** Node.js, Express.js 5.x
- **Database:** MySQL 8.0+
- **Frontend:** HTML5, CSS3, Vanilla JavaScript
- **AI Integration:** ASI:One API for question generation
- **PDF Generation:** PDFKit
- **Authentication:** JWT-based sessions with bcrypt
- **Caching:** Redis (optional)

**Key Features:**
- Teacher authentication and quiz management
- AI-powered question generation
- Student quiz access with auto-saving
- Real-time scoring and evaluation
- Automated professional PDF certificate generation
- Secure CORS, rate limiting, and XSS protection

**Setup:**
```bash
cd Milestone_project_1/Automated-Quiz-Engine-with-PDF-Certification-/
npm install
# Configure .env file with DB_HOST, DB_USER, DB_PASSWORD, etc.
npm start
```

**Database:** See `database_schema.sql` for schema setup

---

### 2. **Milestone Project 2: Job Portal Management System**

**Location:** [`Milestone_project_2/`](Milestone_project_2/)

A comprehensive web application connecting job seekers and employers with profile management, resume uploads, job listings, applications, and analytics.

**Tech Stack:**
- **Backend:** Spring Boot + Spring MVC
- **Database:** MySQL / H2
- **Frontend:** HTML5/CSS3, Thymeleaf templates, JavaScript (ES6)
- **Security:** Spring Security with Role-Based Access Control (RBAC)
- **ORM:** Spring Data JPA

**Key Modules:**
1. User Management - Job Seeker/Student profiles
2. Employer Management - Employer/Admin dashboards
3. Authentication & Security - Login, authorization, RBAC
4. Job Management - Job posting and management
5. Application Tracking - Track job applications
6. File Upload & Resume - Resume storage and management
7. Notifications - Email and in-app notifications
8. Dashboard & Analytics - Statistics and performance metrics

**Setup:**
```bash
cd Milestone_project_2/Job-portal-managment-system/
# Configure application.properties with database details
mvn clean install
mvn spring-boot:run
```

**Documentation:** See `Docs/` folder for detailed PRD files

---

## 📖 Learning Path by Week

### **Weeks 1-3:** Fundamentals & Basic Concepts
- Introduction to Java and Spring Boot
- REST API basics
- Database fundamentals

### **Weeks 4-6:** Core Spring Boot Development
- **Session 16-20:** Spring Boot project structure and configuration
- **Session 21-25:** Advanced Spring Boot patterns
- **Session 26-30:** Integration and testing

### **Week 7:** Introduction to Microservices (Sessions 31-35)

**Services:**
- `service-registry/` - Service discovery mechanism
- `user-service/` - User management microservice
- `product-service/` - Product management microservice

**Learning Focus:**
- Microservices architecture principles
- Inter-service communication
- Service registration and discovery

### **Week 8:** Advanced Microservices Architecture (Sessions 36-40)

**Services:**
- `discovery-ms/` - Eureka service discovery
- `api-gateway/` - API Gateway for request routing
- `user-ms/` - Enhanced user microservice
- `accountmng-ms/` - Account management microservice

**Learning Focus:**
- API Gateway pattern
- Load balancing
- Service-to-service communication
- Distributed system patterns

---

## 🎯 Assigned Task

**Location:** [`Assigned_Task_18/`](Assigned_Task_18/)

A practical implementation task using Spring Boot 3.5.13 and Java 17.

**Setup:**
```bash
cd Assigned_Task_18/
mvn clean install
mvn spring-boot:run
```

---

## 🛠️ Technology Stack Summary

| Category | Technologies |
|----------|--------------|
| **Languages** | Java 17+, JavaScript (ES6+) |
| **Backend Frameworks** | Spring Boot, Express.js |
| **Databases** | MySQL 8.0+, H2 (test) |
| **Architecture** | Microservices, REST APIs |
| **Security** | Spring Security, JWT, bcrypt |
| **Build Tools** | Maven, npm |
| **Frontend** | HTML5, CSS3, Vanilla JS, Thymeleaf |
| **Additional** | Docker, Eureka, API Gateway |

---

## 📋 Prerequisites & Requirements

### General Requirements
- **Git** (version control)
- **Java Development Kit (JDK):** 17 or higher
- **Node.js:** 18.0.0 or higher (for Milestone Project 1)
- **npm:** 9.0.0 or higher
- **Maven:** 3.8.0 or higher
- **MySQL:** 8.0 or higher

### Optional Tools
- **Docker & Docker Compose** (for containerization)
- **Redis** (for caching and distributed rate limiting)
- **Postman** (for API testing)

### API Keys
- **ASI:One API Key** (for Milestone Project 1 AI features) - Free tier at [asi1.ai](https://asi1.ai)

---

## 🔧 Quick Start Guide

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Fullstack-development-using-Java
```

### 2. Run a Spring Boot Project
```bash
# Example: Assigned Task 18
cd Assigned_Task_18
mvn clean install
mvn spring-boot:run
# API accessible at http://localhost:8080
```

### 3. Run a Node.js Project
```bash
# Example: Milestone Project 1
cd Milestone_project_1/Automated-Quiz-Engine-with-PDF-Certification-/
npm install
# Create and configure .env file
npm start
# Application accessible at http://localhost:3000
```

### 4. Run Microservices
```bash
# Each service runs independently
cd week\ 8/session\ 36\ -\ 40/discovery-ms/
mvn spring-boot:run

# In separate terminals, run:
cd api-gateway/
mvn spring-boot:run

cd user-ms/
mvn spring-boot:run

cd accountmng-ms/
mvn spring-boot:run
```

---

## 📁 Directory Structure

```
.
├── LICENSE                          # MIT License
├── README.md                        # This file
├── Assigned_Task_18/                # Spring Boot assignment
│   ├── pom.xml
│   └── src/
├── Milestone_project_1/             # Quiz Engine with PDF Certification
│   └── Automated-Quiz-Engine-with-PDF-Certification-/
│       ├── package.json
│       ├── server.js
│       ├── database_schema.sql
│       └── public/
├── Milestone_project_2/             # Job Portal Management System
│   └── Job-portal-managment-system/
│       ├── pom.xml
│       ├── src/
│       └── Docs/
├── week 1/                          # Learning progression Week 1
├── week 2/                          # Learning progression Week 2
├── ...
├── week 7/                          # Microservices: product, user, registry
│   └── session 31 - 35/
│       ├── product-service/
│       ├── service-registry/
│       └── user-service/
└── week 8/                          # Advanced Microservices: API Gateway
    └── session 36 - 40/
        ├── api-gateway/
        ├── discovery-ms/
        ├── user-ms/
        └── accountmng-ms/
```

---

## 💡 Key Learning Concepts

### **Spring Boot & Java**
- Project initialization and configuration
- Dependency injection and inversion of control
- REST API design and implementation
- Spring Security and authentication
- Spring Data JPA and database operations
- Exception handling and logging

### **Microservices Architecture**
- Service decomposition
- Service discovery and registration
- API Gateway pattern
- Inter-service communication
- Load balancing
- Distributed system patterns

### **Full-Stack Development**
- Backend API design
- Frontend template rendering (Thymeleaf)
- Database schema design
- Authentication and authorization
- File upload and management

### **Frontend Technologies**
- HTML5 semantic markup
- CSS3 responsive design
- Vanilla JavaScript ES6+
- DOM manipulation and events
- AJAX and asynchronous programming

---

## 🧪 Testing

### Unit Testing
Each project includes test configurations:
- **Spring Boot projects:** JUnit, Mockito
- **Node.js projects:** Jest, Supertest (if configured)

### Running Tests
```bash
# Maven projects
mvn test

# npm projects
npm test
```

---

## 🔐 Security Considerations

- **JWT Authentication:** Secure token-based authentication
- **Spring Security:** Role-based access control (RBAC)
- **Password Hashing:** bcrypt for password security
- **CORS Protection:** Configurable cross-origin policies
- **SQL Injection Prevention:** Parameterized queries
- **XSS Protection:** HTML sanitization
- **Rate Limiting:** Request throttling mechanisms

---

## 📚 Additional Resources

### Milestone Project 1
- See [`Milestone_project_1/README.md`](Milestone_project_1/Automated-Quiz-Engine-with-PDF-Certification-/README.md) for detailed setup

### Milestone Project 2
- See [`Milestone_project_2/Docs/`](Milestone_project_2/Job-portal-managment-system/Docs/) for PRD files
- See [`Milestone_project_2/README.md`](Milestone_project_2/Job-portal-managment-system/README.md)

### Each Week's Sessions
- Each week folder contains session-specific HELP.md files
- Review HELP.md in each session for setup and execution instructions

---

## 🤝 Contributing

This is a personal learning repository. For improvements or corrections, feel free to document them.

---

## 📝 Notes

- All projects follow industry-standard practices
- Code demonstrates real-world application architecture
- Progressive complexity from basic to advanced concepts
- Each project is independent and can be run separately
- Database credentials should be configured via environment variables or configuration files

---

## ❓ Troubleshooting

### Port Already in Use
```bash
# Change port in application.properties or pom.xml
server.port=8081
```

### Database Connection Issues
- Verify MySQL is running
- Check database credentials in configuration files
- Ensure database exists (run schema SQL files if needed)

### Dependency Issues
```bash
# Clear and rebuild
mvn clean install -U
npm install --force
```

### API Gateway Issues
- Ensure discovery service (Eureka) is running first
- Check service registration status in Eureka dashboard

---

## 📞 Support

For individual project issues:
- Check the project's specific README.md
- Review the HELP.md files in week sessions
- Consult the Docs/ folders in milestone projects

---

**Happy Learning! 🚀**

*Last Updated: April 2026*
