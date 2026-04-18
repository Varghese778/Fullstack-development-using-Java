package com.jobportal.service;

import com.jobportal.dto.request.*;
import com.jobportal.dto.response.EmployerResponse;
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
public class EmployerService {

    private final EmployerRepository employerRepository;
    private final OfficeLocationRepository officeLocationRepository;
    private final EmployerSocialLinkRepository socialLinkRepository;
    private final TeamMemberRepository teamMemberRepository;

    public Employer getEmployerById(Long employerId) {
        return employerRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Employer", "id", employerId));
    }

    public Employer getEmployerByEmail(String email) {
        return employerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employer", "email", email));
    }

    public EmployerResponse toResponse(Employer e) {
        return EmployerResponse.builder()
                .employerId(e.getEmployerId()).email(e.getEmail()).companyName(e.getCompanyName())
                .companyWebsite(e.getCompanyWebsite()).phoneNumber(e.getPhoneNumber())
                .industry(e.getIndustry() != null ? e.getIndustry().name() : null)
                .companySize(e.getCompanySize() != null ? e.getCompanySize().name() : null)
                .headquartersLocation(e.getHeadquartersLocation()).description(e.getDescription())
                .logoUrl(e.getLogoUrl()).isVerified(e.getIsVerified())
                .approvalStatus(e.getApprovalStatus() != null ? e.getApprovalStatus().name() : null)
                .contactPerson(e.getContactPerson()).contactEmail(e.getContactEmail())
                .foundedYear(e.getFoundedYear()).isActive(e.getIsActive()).createdAt(e.getCreatedAt())
                .build();
    }

    public Employer updateProfile(Long employerId, EmployerProfileRequest request) {
        Employer emp = getEmployerById(employerId);
        if (request.getCompanyName() != null) emp.setCompanyName(request.getCompanyName());
        if (request.getCompanyWebsite() != null) emp.setCompanyWebsite(request.getCompanyWebsite());
        if (request.getPhoneNumber() != null) emp.setPhoneNumber(request.getPhoneNumber());
        if (request.getHeadquartersLocation() != null) emp.setHeadquartersLocation(request.getHeadquartersLocation());
        if (request.getDescription() != null) emp.setDescription(request.getDescription());
        if (request.getFoundedYear() != null) emp.setFoundedYear(request.getFoundedYear());
        if (request.getCulture() != null) emp.setCulture(request.getCulture());
        if (request.getBenefits() != null) emp.setBenefits(request.getBenefits());
        if (request.getContactPerson() != null) emp.setContactPerson(request.getContactPerson());
        if (request.getContactEmail() != null) emp.setContactEmail(request.getContactEmail());
        if (request.getGstNumber() != null) emp.setGstNumber(request.getGstNumber());
        if (request.getIndustry() != null) {
            try { emp.setIndustry(IndustryEnum.valueOf(request.getIndustry())); } catch (Exception ignored) {}
        }
        if (request.getCompanySize() != null) {
            try { emp.setCompanySize(CompanySizeEnum.valueOf(request.getCompanySize())); } catch (Exception ignored) {}
        }
        return employerRepository.save(emp);
    }

    public void verifyEmployer(Long employerId, Long adminId) {
        Employer emp = getEmployerById(employerId);
        emp.setIsVerified(true);
        emp.setApprovalStatus(ApprovalStatusEnum.APPROVED);
        emp.setApprovedBy(adminId);
        emp.setVerificationDate(LocalDateTime.now());
        employerRepository.save(emp);
        log.info("Employer verified: {} by admin {}", emp.getCompanyName(), adminId);
    }

    public void rejectEmployer(Long employerId, Long adminId) {
        Employer emp = getEmployerById(employerId);
        emp.setIsVerified(false);
        emp.setApprovalStatus(ApprovalStatusEnum.REJECTED);
        emp.setApprovedBy(adminId);
        employerRepository.save(emp);
    }

    public OfficeLocation addOfficeLocation(Long employerId, OfficeLocationRequest request) {
        Employer emp = getEmployerById(employerId);
        OfficeLocation loc = OfficeLocation.builder()
                .employer(emp).locationName(request.getLocationName()).address(request.getAddress())
                .city(request.getCity()).state(request.getState()).pincode(request.getPincode())
                .country(request.getCountry() != null ? request.getCountry() : "India")
                .isHeadquarters(request.getIsHeadquarters() != null ? request.getIsHeadquarters() : false)
                .build();
        return officeLocationRepository.save(loc);
    }

    public void deleteOfficeLocation(Long locationId) {
        officeLocationRepository.deleteById(locationId);
    }

    public List<OfficeLocation> getOfficeLocations(Long employerId) {
        return officeLocationRepository.findByEmployerEmployerId(employerId);
    }

    public TeamMember addTeamMember(Long employerId, TeamMemberRequest request) {
        Employer emp = getEmployerById(employerId);
        TeamMember member = TeamMember.builder()
                .employer(emp).email(request.getEmail()).name(request.getName()).role(request.getRole()).build();
        return teamMemberRepository.save(member);
    }

    public void removeTeamMember(Long memberId) {
        TeamMember member = teamMemberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamMember", "id", memberId));
        member.setIsActive(false);
        teamMemberRepository.save(member);
    }

    public List<TeamMember> getTeamMembers(Long employerId) {
        return teamMemberRepository.findByEmployerEmployerIdAndIsActive(employerId, true);
    }

    public Page<Employer> searchEmployers(String keyword, Pageable pageable) {
        return employerRepository.searchEmployers(keyword, pageable);
    }

    public Page<Employer> getEmployersByStatus(ApprovalStatusEnum status, Pageable pageable) {
        return employerRepository.findByApprovalStatus(status, pageable);
    }

    public Page<Employer> getAllEmployers(Pageable pageable) {
        return employerRepository.findAll(pageable);
    }
}
