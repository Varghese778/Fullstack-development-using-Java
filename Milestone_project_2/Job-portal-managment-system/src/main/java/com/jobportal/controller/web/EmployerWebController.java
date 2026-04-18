package com.jobportal.controller.web;

import com.jobportal.dto.request.*;
import com.jobportal.entity.Employer;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employer")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerWebController {

    private final EmployerService employerService;
    private final AnalyticsService analyticsService;
    private final JobService jobService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Long empId = SecurityUtils.getCurrentUserId();
        Employer employer = employerService.getEmployerById(empId);
        model.addAttribute("employer", employer);
        model.addAttribute("stats", analyticsService.getEmployerDashboard(empId));
        return "employer/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Long empId = SecurityUtils.getCurrentUserId();
        Employer employer = employerService.getEmployerById(empId);
        model.addAttribute("employer", employer);
        model.addAttribute("locations", employerService.getOfficeLocations(empId));
        model.addAttribute("teamMembers", employerService.getTeamMembers(empId));
        return "employer/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        Long empId = SecurityUtils.getCurrentUserId();
        model.addAttribute("employer", employerService.getEmployerById(empId));
        return "employer/edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute EmployerProfileRequest request, RedirectAttributes redirectAttributes) {
        Long empId = SecurityUtils.getCurrentUserId();
        employerService.updateProfile(empId, request);
        redirectAttributes.addFlashAttribute("message", "Company profile updated!");
        return "redirect:/employer/profile";
    }

    @PostMapping("/locations/add")
    public String addLocation(@ModelAttribute OfficeLocationRequest request, RedirectAttributes redirectAttributes) {
        Long empId = SecurityUtils.getCurrentUserId();
        employerService.addOfficeLocation(empId, request);
        redirectAttributes.addFlashAttribute("message", "Office location added!");
        return "redirect:/employer/profile";
    }

    @PostMapping("/locations/{id}/delete")
    public String deleteLocation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employerService.deleteOfficeLocation(id);
        redirectAttributes.addFlashAttribute("message", "Location removed.");
        return "redirect:/employer/profile";
    }

    @PostMapping("/team/add")
    public String addTeamMember(@ModelAttribute TeamMemberRequest request, RedirectAttributes redirectAttributes) {
        Long empId = SecurityUtils.getCurrentUserId();
        employerService.addTeamMember(empId, request);
        redirectAttributes.addFlashAttribute("message", "Team member added!");
        return "redirect:/employer/profile";
    }

    @PostMapping("/team/{id}/remove")
    public String removeTeamMember(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employerService.removeTeamMember(id);
        redirectAttributes.addFlashAttribute("message", "Team member removed.");
        return "redirect:/employer/profile";
    }
}
