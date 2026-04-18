package com.jobportal.service;

import com.jobportal.dto.request.ApplicationRequest;
import com.jobportal.dto.request.InterviewScheduleRequest;
import com.jobportal.dto.response.ApplicationResponse;
import com.jobportal.entity.*;
import com.jobportal.enums.*;
import com.jobportal.exception.*;
import com.jobportal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationHistoryRepository historyRepository;
    private final InterviewScheduleRepository interviewRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;

    public Application submitApplication(Long userId, Long jobId, ApplicationRequest request) {
        // Prevent duplicate applications
        if (applicationRepository.existsByJobJobIdAndUserUserIdAndStatusNot(jobId, userId, ApplicationStatusEnum.WITHDRAWN)) {
            throw new DuplicateResourceException("You have already applied for this job");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Application app = Application.builder()
                .job(job).user(user).coverLetter(request.getCoverLetter())
                .status(ApplicationStatusEnum.SUBMITTED)
                .build();

        if (request.getResumeId() != null) {
            Resume resume = resumeRepository.findById(request.getResumeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Resume", "id", request.getResumeId()));
            app.setResume(resume);
            resume.setApplicationCount(resume.getApplicationCount() + 1);
            resumeRepository.save(resume);
        }

        Application saved = applicationRepository.save(app);
        // Increment job application count
        job.setApplicationCount(job.getApplicationCount() + 1);
        jobRepository.save(job);
        // Record history
        addHistory(saved, null, ApplicationStatusEnum.SUBMITTED, userId, "Application submitted");
        log.info("Application submitted: user {} for job {}", userId, jobId);
        return saved;
    }

    public Application getApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));
    }

    public void updateStatus(Long applicationId, ApplicationStatusEnum newStatus, Long changedBy, String reason) {
        Application app = getApplication(applicationId);
        ApplicationStatusEnum oldStatus = app.getStatus();
        app.setStatus(newStatus);
        app.setStatusUpdateDate(LocalDateTime.now());
        if (newStatus == ApplicationStatusEnum.REJECTED) {
            app.setRejectionDate(LocalDateTime.now());
        }
        applicationRepository.save(app);
        addHistory(app, oldStatus, newStatus, changedBy, reason);
    }

    public void shortlistApplication(Long applicationId, Long changedBy, String notes) {
        updateStatus(applicationId, ApplicationStatusEnum.SHORTLISTED, changedBy, "Candidate shortlisted");
        if (notes != null) {
            Application app = getApplication(applicationId);
            app.setNotes(notes);
            applicationRepository.save(app);
        }
    }

    public void rejectApplication(Long applicationId, Long changedBy, RejectionReasonEnum reason, String message) {
        Application app = getApplication(applicationId);
        app.setRejectionReason(reason);
        applicationRepository.save(app);
        updateStatus(applicationId, ApplicationStatusEnum.REJECTED, changedBy, message != null ? message : reason.name());
    }

    public void withdrawApplication(Long applicationId, Long userId, String reason) {
        Application app = getApplication(applicationId);
        if (!app.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only withdraw your own applications");
        }
        app.setWithdrawnDate(LocalDateTime.now());
        app.setWithdrawalReason(reason);
        applicationRepository.save(app);
        updateStatus(applicationId, ApplicationStatusEnum.WITHDRAWN, userId, reason);
    }

    public InterviewSchedule scheduleInterview(Long applicationId, InterviewScheduleRequest request, Long createdBy) {
        Application app = getApplication(applicationId);
        InterviewSchedule schedule = InterviewSchedule.builder()
                .application(app)
                .interviewDate(request.getInterviewDate())
                .interviewTime(request.getInterviewTime())
                .interviewLocation(request.getInterviewLocation())
                .videoCallLink(request.getVideoCallLink())
                .panelMembers(request.getPanelMembers())
                .notes(request.getNotes())
                .createdBy(createdBy)
                .build();

        if (request.getInterviewType() != null) {
            try { schedule.setInterviewType(InterviewTypeEnum.valueOf(request.getInterviewType())); } catch (Exception ignored) {}
        }

        InterviewSchedule saved = interviewRepository.save(schedule);
        updateStatus(applicationId, ApplicationStatusEnum.INTERVIEWED, createdBy, "Interview scheduled");
        app.setInterviewScheduledDate(LocalDateTime.of(request.getInterviewDate(), request.getInterviewTime()));
        applicationRepository.save(app);
        return saved;
    }

    public Page<Application> getUserApplications(Long userId, Pageable pageable) {
        return applicationRepository.findByUserUserId(userId, pageable);
    }

    public Page<Application> getJobApplications(Long jobId, Pageable pageable) {
        return applicationRepository.findByJobJobId(jobId, pageable);
    }

    public Page<Application> getEmployerApplications(Long employerId, Pageable pageable) {
        return applicationRepository.findByEmployerId(employerId, pageable);
    }

    public Page<Application> getEmployerApplicationsByStatus(Long employerId, ApplicationStatusEnum status, Pageable pageable) {
        return applicationRepository.findByEmployerIdAndStatus(employerId, status, pageable);
    }

    public List<ApplicationHistory> getApplicationHistory(Long applicationId) {
        return historyRepository.findByApplicationApplicationIdOrderByChangedAtDesc(applicationId);
    }

    public void bulkUpdateStatus(List<Long> applicationIds, ApplicationStatusEnum status, Long changedBy) {
        applicationIds.forEach(id -> updateStatus(id, status, changedBy, "Bulk status update"));
    }

    public ApplicationResponse toResponse(Application app) {
        return ApplicationResponse.builder()
                .applicationId(app.getApplicationId())
                .jobId(app.getJob().getJobId())
                .jobTitle(app.getJob().getJobTitle())
                .companyName(app.getJob().getCompanyName())
                .companyLogo(app.getJob().getCompanyLogo())
                .userId(app.getUser().getUserId())
                .candidateName(app.getUser().getFullName())
                .candidateEmail(app.getUser().getEmail())
                .status(app.getStatus().name())
                .coverLetter(app.getCoverLetter())
                .rating(app.getRating())
                .isStarred(app.getIsStarred())
                .resumeId(app.getResume() != null ? app.getResume().getResumeId() : null)
                .resumeName(app.getResume() != null ? app.getResume().getResumeName() : null)
                .applicationDate(app.getApplicationDate())
                .statusUpdateDate(app.getStatusUpdateDate())
                .rejectionReason(app.getRejectionReason() != null ? app.getRejectionReason().name() : null)
                .interviewType(app.getInterviewType() != null ? app.getInterviewType().name() : null)
                .interviewScheduledDate(app.getInterviewScheduledDate())
                .build();
    }

    private void addHistory(Application app, ApplicationStatusEnum oldStatus, ApplicationStatusEnum newStatus, Long changedBy, String reason) {
        ApplicationHistory history = ApplicationHistory.builder()
                .application(app).oldStatus(oldStatus).newStatus(newStatus)
                .changedBy(changedBy).changedAt(LocalDateTime.now()).changeReason(reason)
                .build();
        historyRepository.save(history);
    }
}
