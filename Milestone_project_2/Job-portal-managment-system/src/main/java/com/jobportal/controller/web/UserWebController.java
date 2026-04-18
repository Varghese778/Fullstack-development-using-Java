package com.jobportal.controller.web;

import com.jobportal.dto.request.*;
import com.jobportal.entity.*;
import com.jobportal.security.CustomUserDetails;
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
@RequiredArgsConstructor
@Slf4j
public class UserWebController {

    private final UserService userService;
    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentDashboard(Model model) {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("stats", analyticsService.getStudentDashboard(userId));
        return "user/dashboard";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public String profilePage(Model model) {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("educations", userService.getEducations(userId));
        model.addAttribute("experiences", userService.getExperiences(userId));
        model.addAttribute("skills", userService.getSkills(userId));
        return "user/profile";
    }

    @GetMapping("/profile/edit")
    @PreAuthorize("hasRole('STUDENT')")
    public String editProfilePage(Model model) {
        Long userId = SecurityUtils.getCurrentUserId();
        model.addAttribute("user", userService.getUserById(userId));
        return "user/edit-profile";
    }

    @PostMapping("/profile/edit")
    @PreAuthorize("hasRole('STUDENT')")
    public String updateProfile(@ModelAttribute UserProfileRequest request, RedirectAttributes redirectAttributes) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.updateProfile(userId, request);
        redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
        return "redirect:/profile";
    }

    @PostMapping("/profile/education/add")
    @PreAuthorize("hasRole('STUDENT')")
    public String addEducation(@ModelAttribute EducationRequest request, RedirectAttributes redirectAttributes) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.addEducation(userId, request);
        redirectAttributes.addFlashAttribute("message", "Education added!");
        return "redirect:/profile";
    }

    @PostMapping("/profile/education/{id}/delete")
    @PreAuthorize("hasRole('STUDENT')")
    public String deleteEducation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteEducation(id);
        redirectAttributes.addFlashAttribute("message", "Education removed.");
        return "redirect:/profile";
    }

    @PostMapping("/profile/experience/add")
    @PreAuthorize("hasRole('STUDENT')")
    public String addExperience(@ModelAttribute ExperienceRequest request, RedirectAttributes redirectAttributes) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.addExperience(userId, request);
        redirectAttributes.addFlashAttribute("message", "Experience added!");
        return "redirect:/profile";
    }

    @PostMapping("/profile/experience/{id}/delete")
    @PreAuthorize("hasRole('STUDENT')")
    public String deleteExperience(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteExperience(id);
        redirectAttributes.addFlashAttribute("message", "Experience removed.");
        return "redirect:/profile";
    }

    @PostMapping("/profile/skills/add")
    @PreAuthorize("hasRole('STUDENT')")
    public String addSkill(@ModelAttribute SkillRequest request, RedirectAttributes redirectAttributes) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.addSkill(userId, request);
        redirectAttributes.addFlashAttribute("message", "Skill added!");
        return "redirect:/profile";
    }

    @PostMapping("/profile/skills/{id}/delete")
    @PreAuthorize("hasRole('STUDENT')")
    public String deleteSkill(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteSkill(id);
        redirectAttributes.addFlashAttribute("message", "Skill removed.");
        return "redirect:/profile";
    }
}
