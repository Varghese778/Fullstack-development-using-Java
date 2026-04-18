package com.jobportal.controller.web;

import com.jobportal.dto.request.PasswordResetRequest;
import com.jobportal.dto.request.RegisterRequest;
import com.jobportal.enums.RoleEnum;
import com.jobportal.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthWebController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "expired", required = false) String expired,
                            Model model) {
        if (error != null) model.addAttribute("error", error);
        if (logout != null) model.addAttribute("message", "You have been logged out successfully.");
        if (expired != null) model.addAttribute("error", "Your session has expired. Please log in again.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(@RequestParam(value = "type", defaultValue = "student") String type, Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        model.addAttribute("type", type);
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request, BindingResult result,
                           @RequestParam(value = "type", defaultValue = "student") String type,
                           Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("type", type);
            return "auth/register";
        }
        try {
            if ("employer".equals(type)) {
                request.setRole(RoleEnum.EMPLOYER);
                authService.registerEmployer(request);
            } else {
                request.setRole(RoleEnum.STUDENT);
                authService.registerStudent(request);
            }
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("type", type);
            return "auth/register";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            authService.initiatePasswordReset(email);
            redirectAttributes.addFlashAttribute("message", "Password reset link has been sent to your email.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        model.addAttribute("passwordResetRequest", new PasswordResetRequest());
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @ModelAttribute PasswordResetRequest request, BindingResult result,
                                Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("token", request.getToken());
            return "auth/reset-password";
        }
        try {
            authService.resetPassword(request);
            redirectAttributes.addFlashAttribute("message", "Password reset successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", request.getToken());
            return "auth/reset-password";
        }
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}
