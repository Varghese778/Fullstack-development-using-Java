package com.jobportal.service;

import com.jobportal.dto.request.JobPostRequest;
import com.jobportal.dto.response.JobResponse;
import com.jobportal.entity.*;
import com.jobportal.enums.*;
import com.jobportal.exception.*;
import com.jobportal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JobService {

    private final JobRepository jobRepository;
    private final JobLocationRepository jobLocationRepository;
    private final JobSkillRepository jobSkillRepository;
    private final JobViewHistoryRepository viewHistoryRepository;
    private final EmployerRepository employerRepository;

    public Job createJob(Long employerId, JobPostRequest request) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Employer", "id", employerId));

        Job job = Job.builder()
                .employer(employer)
                .jobTitle(request.getJobTitle())
                .jobDescription(request.getJobDescription())
                .department(request.getDepartment())
                .companyName(employer.getCompanyName())
                .companyLogo(employer.getLogoUrl())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .numberOfPositions(request.getNumberOfPositions() != null ? request.getNumberOfPositions() : 1)
                .applicationDeadline(request.getApplicationDeadline())
                .benefits(request.getBenefits())
                .skillsRequired(request.getSkillsRequired())
                .reportingManager(request.getReportingManager())
                .externalJobUrl(request.getExternalJobUrl())
                .status(JobStatusEnum.DRAFT)
                .createdBy(employerId)
                .build();

        if (request.getCategory() != null) {
            try { job.setCategory(IndustryEnum.valueOf(request.getCategory())); } catch (Exception ignored) {}
        }
        if (request.getExperienceLevel() != null) {
            try { job.setExperienceLevel(ExperienceLevelEnum.valueOf(request.getExperienceLevel())); } catch (Exception ignored) {}
        }
        if (request.getEmploymentType() != null) {
            try { job.setEmploymentType(EmploymentTypeEnum.valueOf(request.getEmploymentType())); } catch (Exception ignored) {}
        }
        if (request.getRemotePolicy() != null) {
            try { job.setRemotePolicy(RemotePolicyEnum.valueOf(request.getRemotePolicy())); } catch (Exception ignored) {}
        }

        Job saved = jobRepository.save(job);
        log.info("Job created: {} by employer {}", saved.getJobTitle(), employerId);
        return saved;
    }

    public Job updateJob(Long jobId, JobPostRequest request) {
        Job job = getJobById(jobId);
        if (request.getJobTitle() != null) job.setJobTitle(request.getJobTitle());
        if (request.getJobDescription() != null) job.setJobDescription(request.getJobDescription());
        if (request.getDepartment() != null) job.setDepartment(request.getDepartment());
        if (request.getSalaryMin() != null) job.setSalaryMin(request.getSalaryMin());
        if (request.getSalaryMax() != null) job.setSalaryMax(request.getSalaryMax());
        if (request.getNumberOfPositions() != null) job.setNumberOfPositions(request.getNumberOfPositions());
        if (request.getApplicationDeadline() != null) job.setApplicationDeadline(request.getApplicationDeadline());
        if (request.getBenefits() != null) job.setBenefits(request.getBenefits());
        if (request.getSkillsRequired() != null) job.setSkillsRequired(request.getSkillsRequired());
        if (request.getExperienceLevel() != null) {
            try { job.setExperienceLevel(ExperienceLevelEnum.valueOf(request.getExperienceLevel())); } catch (Exception ignored) {}
        }
        if (request.getEmploymentType() != null) {
            try { job.setEmploymentType(EmploymentTypeEnum.valueOf(request.getEmploymentType())); } catch (Exception ignored) {}
        }
        return jobRepository.save(job);
    }

    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));
    }

    public void publishJob(Long jobId) {
        Job job = getJobById(jobId);
        job.setStatus(JobStatusEnum.ACTIVE);
        job.setIsPublished(true);
        job.setPublishedDate(LocalDateTime.now());
        jobRepository.save(job);
    }

    public void closeJob(Long jobId) {
        Job job = getJobById(jobId);
        job.setStatus(JobStatusEnum.CLOSED);
        job.setClosedDate(LocalDateTime.now());
        jobRepository.save(job);
    }

    public void archiveJob(Long jobId) {
        Job job = getJobById(jobId);
        job.setStatus(JobStatusEnum.ARCHIVED);
        jobRepository.save(job);
    }

    public void incrementViewCount(Long jobId, Long userId) {
        Job job = getJobById(jobId);
        // Prevent duplicate views within 1 hour
        var existing = viewHistoryRepository.findByJobJobIdAndUserIdAndViewedAtAfter(
                jobId, userId, LocalDateTime.now().minusHours(1));
        if (existing.isEmpty()) {
            job.setViewCount((job.getViewCount() != null ? job.getViewCount() : 0) + 1);
            jobRepository.save(job);
            viewHistoryRepository.save(JobViewHistory.builder()
                    .job(job).userId(userId).viewedAt(LocalDateTime.now()).build());
        }
    }

    public Page<Job> getActiveJobs(Pageable pageable) {
        return jobRepository.findByStatus(JobStatusEnum.ACTIVE, pageable);
    }

    public Page<Job> searchJobs(String keyword, Pageable pageable) {
        return jobRepository.searchByKeyword(keyword, pageable);
    }

    public Page<Job> getEmployerJobs(Long employerId, Pageable pageable) {
        return jobRepository.findByEmployerEmployerId(employerId, pageable);
    }

    public Page<Job> getEmployerJobsByStatus(Long employerId, JobStatusEnum status, Pageable pageable) {
        return jobRepository.findByEmployerEmployerIdAndStatus(employerId, status, pageable);
    }

    public List<Job> getTrendingJobs(int limit) {
        return jobRepository.findTrendingJobs(PageRequest.of(0, limit));
    }

    public JobResponse toResponse(Job job) {
        List<String> locations = job.getLocations() != null ?
                job.getLocations().stream().map(l -> l.getCity() + ", " + l.getState()).collect(Collectors.toList()) :
                List.of();
        List<String> skills = job.getRequiredSkills() != null ?
                job.getRequiredSkills().stream().map(JobSkill::getSkillName).collect(Collectors.toList()) :
                List.of();

        return JobResponse.builder()
                .jobId(job.getJobId()).jobTitle(job.getJobTitle())
                .companyName(job.getCompanyName()).companyLogo(job.getCompanyLogo())
                .department(job.getDepartment())
                .category(job.getCategory() != null ? job.getCategory().name() : null)
                .salaryMin(job.getSalaryMin()).salaryMax(job.getSalaryMax())
                .experienceLevel(job.getExperienceLevel() != null ? job.getExperienceLevel().name() : null)
                .employmentType(job.getEmploymentType() != null ? job.getEmploymentType().name() : null)
                .remotePolicy(job.getRemotePolicy() != null ? job.getRemotePolicy().name() : null)
                .status(job.getStatus().name())
                .viewCount(job.getViewCount()).applicationCount(job.getApplicationCount())
                .numberOfPositions(job.getNumberOfPositions())
                .applicationDeadline(job.getApplicationDeadline() != null ? job.getApplicationDeadline().toString() : null)
                .locations(locations).skills(skills)
                .publishedDate(job.getPublishedDate())
                .createdAt(job.getCreatedAt())
                .employerId(job.getEmployer().getEmployerId())
                .build();
    }

    // Scheduled: expire jobs past deadline
    @Scheduled(cron = "0 0 0 * * *")
    public void expireJobs() {
        List<Job> expired = jobRepository.findExpiredJobs(LocalDate.now());
        expired.forEach(job -> {
            job.setStatus(JobStatusEnum.EXPIRED);
            jobRepository.save(job);
            log.info("Job expired: {}", job.getJobTitle());
        });
    }
}
