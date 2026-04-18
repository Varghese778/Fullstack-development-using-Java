package com.jobportal.service;

import com.jobportal.dto.response.ResumeResponse;
import com.jobportal.entity.*;
import com.jobportal.exception.*;
import com.jobportal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeDownloadLogRepository downloadLogRepository;
    private final UserRepository userRepository;

    @Value("${upload.path:./uploads/resumes}")
    private String uploadPath;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_TYPES = List.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    public Resume uploadResume(Long userId, MultipartFile file, boolean isPrimary) {
        validateFile(file);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        String checksum = computeChecksum(file);
        resumeRepository.findByChecksumHash(checksum).ifPresent(existing -> {
            if (existing.getUser().getUserId().equals(userId) && existing.getIsActive()) {
                throw new DuplicateResourceException("This file has already been uploaded");
            }
        });

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : ".pdf";
        String storedFilename = userId + "_" + System.currentTimeMillis() + extension;

        try {
            Path uploadDir = Paths.get(uploadPath, userId.toString());
            Files.createDirectories(uploadDir);
            Path filePath = uploadDir.resolve(storedFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            if (isPrimary) {
                resumeRepository.findByUserUserIdAndIsPrimary(userId, true).ifPresent(r -> {
                    r.setIsPrimary(false);
                    resumeRepository.save(r);
                });
            }

            Resume resume = Resume.builder()
                    .user(user)
                    .resumeName(originalFilename != null ? originalFilename.replace(extension, "") : "Resume")
                    .originalFileName(originalFilename)
                    .fileExtension(extension)
                    .fileSize(file.getSize())
                    .filePath(filePath.toString())
                    .mimeType(file.getContentType())
                    .isPrimary(isPrimary)
                    .checksumHash(checksum)
                    .build();

            Resume saved = resumeRepository.save(resume);
            log.info("Resume uploaded: {} for user {}", saved.getResumeName(), userId);
            return saved;
        } catch (IOException e) {
            throw new FileStorageException("Failed to upload resume: " + e.getMessage(), e);
        }
    }

    public List<ResumeResponse> getUserResumes(Long userId) {
        return resumeRepository.findByUserUserIdAndIsActiveOrderByUploadedDateDesc(userId, true)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Resume getResume(Long resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume", "id", resumeId));
    }

    public void deleteResume(Long resumeId) {
        Resume resume = getResume(resumeId);
        resume.setIsActive(false);
        resumeRepository.save(resume);
    }

    public void setPrimaryResume(Long userId, Long resumeId) {
        resumeRepository.findByUserUserIdAndIsPrimary(userId, true).ifPresent(r -> {
            r.setIsPrimary(false);
            resumeRepository.save(r);
        });
        Resume resume = getResume(resumeId);
        resume.setIsPrimary(true);
        resumeRepository.save(resume);
    }

    public void logDownload(Long resumeId, Long downloadedBy, String ipAddress) {
        Resume resume = getResume(resumeId);
        resume.setDownloadCount(resume.getDownloadCount() + 1);
        resumeRepository.save(resume);
        downloadLogRepository.save(ResumeDownloadLog.builder()
                .resume(resume).downloadedBy(downloadedBy).downloadedAt(LocalDateTime.now()).ipAddress(ipAddress).build());
    }

    public ResumeResponse toResponse(Resume r) {
        return ResumeResponse.builder()
                .resumeId(r.getResumeId()).resumeName(r.getResumeName())
                .originalFileName(r.getOriginalFileName()).fileExtension(r.getFileExtension())
                .fileSize(r.getFileSize()).isPrimary(r.getIsPrimary())
                .downloadCount(r.getDownloadCount()).applicationCount(r.getApplicationCount())
                .uploadedDate(r.getUploadedDate()).build();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) throw new FileStorageException("File is empty");
        if (file.getSize() > MAX_FILE_SIZE) throw new FileStorageException("File size exceeds 5MB limit");
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new FileStorageException("Unsupported file type. Allowed: PDF, DOC, DOCX");
        }
    }

    private String computeChecksum(MultipartFile file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (InputStream is = file.getInputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesRead);
                }
            }
            return HexFormat.of().formatHex(md.digest());
        } catch (Exception e) {
            return null;
        }
    }
}
