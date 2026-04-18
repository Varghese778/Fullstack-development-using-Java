-- ============================================
-- Job Portal — Seed Data (Dev Profile)
-- ============================================

-- Admin user (password: Admin@123)
INSERT INTO users (email, password, first_name, last_name, role, is_active, is_email_verified, phone_number, profile_completeness)
VALUES ('admin@jobportal.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System', 'Admin', 'ADMIN', true, true, '9999999999', 100);

-- Student user (password: Student@123)
INSERT INTO users (email, password, first_name, last_name, role, is_active, is_email_verified, phone_number, bio, location, profile_completeness)
VALUES ('student@jobportal.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John', 'Doe', 'STUDENT', true, true, '9876543210', 'Passionate software developer with experience in Java and Spring Boot.', 'Bangalore, India', 70);

-- Another student (password: Student@123)
INSERT INTO users (email, password, first_name, last_name, role, is_active, is_email_verified, phone_number, bio, location, profile_completeness)
VALUES ('jane@jobportal.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane', 'Smith', 'STUDENT', true, true, '9876543211', 'Full-stack developer skilled in React and Node.js.', 'Mumbai, India', 60);

-- Employer (password: Employer@123)
INSERT INTO employers (email, password, company_name, company_website, phone_number, industry, company_size, headquarters_location, description, is_active, is_email_verified, is_verified, approval_status, contact_person, contact_email, founded_year, role)
VALUES ('employer@jobportal.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'TechVision Solutions', 'https://techvision.example.com', '9112233445', 'IT', 'MEDIUM', 'Bangalore, India', 'Leading IT consulting and software development company specializing in enterprise solutions.', true, true, true, 'APPROVED', 'HR Manager', 'hr@techvision.com', 2015, 'EMPLOYER');

-- Second Employer (password: Employer@123)
INSERT INTO employers (email, password, company_name, company_website, phone_number, industry, company_size, headquarters_location, description, is_active, is_email_verified, is_verified, approval_status, contact_person, contact_email, founded_year, role)
VALUES ('fintech@jobportal.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FinTech Innovations', 'https://fintech.example.com', '9223344556', 'FINANCE', 'LARGE', 'Mumbai, India', 'Pioneering digital payment and financial technology solutions.', true, true, false, 'PENDING', 'Recruiter', 'recruit@fintech.com', 2018, 'EMPLOYER');

-- Education for student
INSERT INTO user_education (user_id, degree, institution, field_of_study, graduation_year) VALUES (2, 'B.Tech', 'IIT Bangalore', 'Computer Science', 2023);
INSERT INTO user_education (user_id, degree, institution, field_of_study, graduation_year) VALUES (2, 'M.Tech', 'IIT Bangalore', 'Software Engineering', 2025);

-- Experience for student
INSERT INTO user_experience (user_id, job_title, company, start_date, end_date, currently_working, description)
VALUES (2, 'Software Intern', 'Google India', '2024-06-01', '2024-12-31', false, 'Worked on cloud platform services using Java and Go.');

-- Skills for student
INSERT INTO user_skills (user_id, skill_name, proficiency_level) VALUES (2, 'Java', 'ADVANCED');
INSERT INTO user_skills (user_id, skill_name, proficiency_level) VALUES (2, 'Spring Boot', 'ADVANCED');
INSERT INTO user_skills (user_id, skill_name, proficiency_level) VALUES (2, 'React', 'INTERMEDIATE');
INSERT INTO user_skills (user_id, skill_name, proficiency_level) VALUES (2, 'MySQL', 'ADVANCED');
INSERT INTO user_skills (user_id, skill_name, proficiency_level) VALUES (2, 'Docker', 'INTERMEDIATE');

-- Jobs posted by employer 1
INSERT INTO jobs (employer_id, job_title, job_description, department, company_name, employment_type, experience_level, remote_policy, salary_min, salary_max, category, status, is_published, published_date, number_of_positions, application_deadline, skills_required, benefits, created_by)
VALUES (1, 'Senior Java Developer', 'We are looking for an experienced Java developer to join our backend team.', 'Engineering', 'TechVision Solutions', 'FULL_TIME', 'THREE_TO_FIVE_YEARS', 'HYBRID', 1200000, 2000000, 'IT', 'ACTIVE', true, CURRENT_TIMESTAMP, 2, '2026-06-30', 'Java, Spring Boot, Microservices, AWS, Docker', 'Health Insurance, Flexible Hours, Annual Bonus, Learning Budget', 1);

INSERT INTO jobs (employer_id, job_title, job_description, department, company_name, employment_type, experience_level, remote_policy, salary_min, salary_max, category, status, is_published, published_date, number_of_positions, application_deadline, skills_required, benefits, created_by)
VALUES (1, 'Frontend React Developer', 'Join our UI/UX team to build beautiful, responsive web applications using React.js.', 'Engineering', 'TechVision Solutions', 'FULL_TIME', 'ONE_TO_TWO_YEARS', 'REMOTE', 800000, 1400000, 'IT', 'ACTIVE', true, CURRENT_TIMESTAMP, 3, '2026-07-15', 'React, TypeScript, CSS, REST APIs, Git', 'Remote Work, Health Insurance, Stock Options', 1);

INSERT INTO jobs (employer_id, job_title, job_description, department, company_name, employment_type, experience_level, remote_policy, salary_min, salary_max, category, status, is_published, published_date, number_of_positions, skills_required, created_by)
VALUES (1, 'DevOps Engineer', 'We need a DevOps engineer to maintain our CI/CD pipeline and manage cloud infrastructure.', 'DevOps', 'TechVision Solutions', 'FULL_TIME', 'THREE_TO_FIVE_YEARS', 'ON_SITE', 1500000, 2200000, 'IT', 'ACTIVE', true, CURRENT_TIMESTAMP, 1, 'Kubernetes, Docker, AWS, Jenkins, Terraform', 1);

INSERT INTO jobs (employer_id, job_title, job_description, department, company_name, employment_type, experience_level, remote_policy, salary_min, salary_max, category, status, is_published, published_date, number_of_positions, skills_required, created_by)
VALUES (2, 'Data Analyst', 'Analyze financial data, build dashboards, and provide insights to drive business decisions.', 'Analytics', 'FinTech Innovations', 'FULL_TIME', 'FRESHER', 'HYBRID', 600000, 900000, 'FINANCE', 'ACTIVE', true, CURRENT_TIMESTAMP, 2, 'Python, SQL, Tableau, Excel, Statistics', 2);

INSERT INTO jobs (employer_id, job_title, job_description, department, company_name, employment_type, experience_level, remote_policy, salary_min, salary_max, category, status, created_by)
VALUES (1, 'QA Engineer (Draft)', 'Quality Assurance engineer position for testing our enterprise products.', 'QA', 'TechVision Solutions', 'FULL_TIME', 'ONE_TO_TWO_YEARS', 'ON_SITE', 700000, 1100000, 'IT', 'DRAFT', 1);

-- Applications
INSERT INTO applications (job_id, user_id, status, cover_letter, application_date)
VALUES (1, 2, 'SHORTLISTED', 'I am excited to apply for the Senior Java Developer position. With 3+ years of experience in Java and Spring Boot, I believe I am an excellent fit for this role.', CURRENT_TIMESTAMP);

INSERT INTO applications (job_id, user_id, status, cover_letter, application_date)
VALUES (2, 2, 'SUBMITTED', 'I am interested in the Frontend React Developer position and have strong skills in React and TypeScript.', CURRENT_TIMESTAMP);

INSERT INTO applications (job_id, user_id, status, cover_letter, application_date)
VALUES (1, 3, 'SUBMITTED', 'I am a passionate developer with exposure to Java backend development.', CURRENT_TIMESTAMP);

-- Application History
INSERT INTO application_history (application_id, old_status, new_status, changed_by, changed_at, change_reason) VALUES (1, null, 'SUBMITTED', 2, CURRENT_TIMESTAMP, 'Application submitted');
INSERT INTO application_history (application_id, old_status, new_status, changed_by, changed_at, change_reason) VALUES (1, 'SUBMITTED', 'SHORTLISTED', 1, CURRENT_TIMESTAMP, 'Candidate shortlisted');

-- Notifications
INSERT INTO notifications (recipient_id, recipient_type, notification_type, subject, message, action_link, priority, is_read) VALUES (2, 'USER', 'APPLICATION_STATUS', 'Application Shortlisted!', 'Your application for Senior Java Developer at TechVision Solutions has been shortlisted.', '/applications/1', 'HIGH', false);
INSERT INTO notifications (recipient_id, recipient_type, notification_type, subject, message, priority, is_read) VALUES (2, 'USER', 'PROFILE_COMPLETION', 'Complete Your Profile', 'Your profile is 70% complete. Add more details to stand out to employers!', 'NORMAL', false);
INSERT INTO notifications (recipient_id, recipient_type, notification_type, subject, message, priority, is_read) VALUES (1, 'EMPLOYER', 'APPLICATION_STATUS', 'New Application Received', 'John Doe has applied for Senior Java Developer position.', 'NORMAL', false);
