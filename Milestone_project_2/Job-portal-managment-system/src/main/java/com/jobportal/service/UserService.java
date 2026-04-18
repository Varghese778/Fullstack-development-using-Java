package com.jobportal.service;

import com.jobportal.dto.request.*;
import com.jobportal.dto.response.UserResponse;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserEducationRepository educationRepository;
    private final UserExperienceRepository experienceRepository;
    private final UserSkillRepository skillRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .location(user.getLocation())
                .role(user.getRole().name())
                .bio(user.getBio())
                .profilePictureUrl(user.getProfilePictureUrl())
                .availabilityStatus(user.getAvailabilityStatus() != null ? user.getAvailabilityStatus().name() : null)
                .profileCompleteness(user.getProfileCompleteness())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }

    public User updateProfile(Long userId, UserProfileRequest request) {
        User user = getUserById(userId);
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getLocation() != null) user.setLocation(request.getLocation());
        if (request.getPortfolioUrl() != null) user.setPortfolioUrl(request.getPortfolioUrl());
        if (request.getLinkedinUrl() != null) user.setLinkedinUrl(request.getLinkedinUrl());
        if (request.getGithubUrl() != null) user.setGithubUrl(request.getGithubUrl());
        if (request.getExpectedSalary() != null) user.setExpectedSalary(request.getExpectedSalary());
        if (request.getAvailabilityStatus() != null) {
            try { user.setAvailabilityStatus(AvailabilityStatusEnum.valueOf(request.getAvailabilityStatus())); } catch (Exception ignored) {}
        }
        user.setProfileCompleteness(calculateProfileCompleteness(user));
        return userRepository.save(user);
    }

    public UserEducation addEducation(Long userId, EducationRequest request) {
        User user = getUserById(userId);
        UserEducation edu = UserEducation.builder()
                .user(user).degree(request.getDegree()).institution(request.getInstitution())
                .fieldOfStudy(request.getFieldOfStudy()).graduationYear(request.getGraduationYear())
                .description(request.getDescription()).build();
        UserEducation saved = educationRepository.save(edu);
        updateProfileCompleteness(userId);
        return saved;
    }

    public void deleteEducation(Long educationId) {
        educationRepository.deleteById(educationId);
    }

    public UserExperience addExperience(Long userId, ExperienceRequest request) {
        User user = getUserById(userId);
        UserExperience exp = UserExperience.builder()
                .user(user).jobTitle(request.getJobTitle()).company(request.getCompany())
                .startDate(request.getStartDate()).endDate(request.getEndDate())
                .currentlyWorking(request.getCurrentlyWorking()).description(request.getDescription())
                .skills(request.getSkills()).build();
        UserExperience saved = experienceRepository.save(exp);
        updateProfileCompleteness(userId);
        return saved;
    }

    public void deleteExperience(Long experienceId) {
        experienceRepository.deleteById(experienceId);
    }

    public UserSkill addSkill(Long userId, SkillRequest request) {
        User user = getUserById(userId);
        UserSkill skill = UserSkill.builder()
                .user(user).skillName(request.getSkillName()).build();
        if (request.getProficiencyLevel() != null) {
            try { skill.setProficiencyLevel(ProficiencyLevelEnum.valueOf(request.getProficiencyLevel())); } catch (Exception ignored) {}
        }
        UserSkill saved = skillRepository.save(skill);
        updateProfileCompleteness(userId);
        return saved;
    }

    public void deleteSkill(Long skillId) {
        skillRepository.deleteById(skillId);
    }

    public List<UserEducation> getEducations(Long userId) {
        return educationRepository.findByUserUserIdOrderByGraduationYearDesc(userId);
    }

    public List<UserExperience> getExperiences(Long userId) {
        return experienceRepository.findByUserUserIdOrderByStartDateDesc(userId);
    }

    public List<UserSkill> getSkills(Long userId) {
        return skillRepository.findByUserUserId(userId);
    }

    public Page<User> searchUsers(String keyword, Pageable pageable) {
        return userRepository.searchUsers(RoleEnum.STUDENT, keyword, pageable);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void deactivateUser(Long userId) {
        User user = getUserById(userId);
        user.setIsActive(false);
        userRepository.save(user);
    }

    public void activateUser(Long userId) {
        User user = getUserById(userId);
        user.setIsActive(true);
        user.setIsAccountLocked(false);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }

    private void updateProfileCompleteness(Long userId) {
        User user = getUserById(userId);
        user.setProfileCompleteness(calculateProfileCompleteness(user));
        userRepository.save(user);
    }

    private int calculateProfileCompleteness(User user) {
        int score = 0;
        if (user.getFirstName() != null) score += 10;
        if (user.getLastName() != null) score += 10;
        if (user.getEmail() != null) score += 10;
        if (user.getPhoneNumber() != null) score += 10;
        if (user.getLocation() != null) score += 10;
        if (user.getBio() != null && !user.getBio().isBlank()) score += 10;
        if (user.getEducations() != null && !user.getEducations().isEmpty()) score += 15;
        if (user.getExperiences() != null && !user.getExperiences().isEmpty()) score += 10;
        if (user.getSkills() != null && !user.getSkills().isEmpty()) score += 10;
        if (user.getResumes() != null && !user.getResumes().isEmpty()) score += 5;
        return Math.min(score, 100);
    }
}
