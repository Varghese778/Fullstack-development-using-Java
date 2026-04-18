package com.jobportal.controller.web;

import com.jobportal.entity.Resume;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.ResumeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/resumes")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('STUDENT')")
public class ResumeWebController {

    private final ResumeService resumeService;

    @GetMapping
    public String resumeManager(Model model) {
        Long userId = SecurityUtils.getCurrentUserId();
        model.addAttribute("resumes", resumeService.getUserResumes(userId));
        return "user/resumes";
    }

    @PostMapping("/upload")
    public String uploadResume(@RequestParam MultipartFile file,
                               @RequestParam(defaultValue = "false") boolean isPrimary,
                               RedirectAttributes redirectAttributes) {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            resumeService.uploadResume(userId, file, isPrimary);
            redirectAttributes.addFlashAttribute("message", "Resume uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/resumes";
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long id, HttpServletRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        Resume resume = resumeService.getResume(id);
        resumeService.logDownload(id, userId, request.getRemoteAddr());

        Path filePath = Paths.get(resume.getFilePath());
        Resource resource = new FileSystemResource(filePath);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resume.getMimeType() != null ? resume.getMimeType() : "application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getOriginalFileName() + "\"")
                .body(resource);
    }

    @PostMapping("/{id}/set-primary")
    public String setPrimary(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Long userId = SecurityUtils.getCurrentUserId();
        resumeService.setPrimaryResume(userId, id);
        redirectAttributes.addFlashAttribute("message", "Primary resume updated!");
        return "redirect:/resumes";
    }

    @PostMapping("/{id}/delete")
    public String deleteResume(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        resumeService.deleteResume(id);
        redirectAttributes.addFlashAttribute("message", "Resume deleted.");
        return "redirect:/resumes";
    }
}
