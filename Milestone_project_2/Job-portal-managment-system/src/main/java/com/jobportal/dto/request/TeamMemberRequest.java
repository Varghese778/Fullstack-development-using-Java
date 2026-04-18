package com.jobportal.dto.request;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TeamMemberRequest {
    private String email;
    private String name;
    private String role; // RECRUITER, MANAGER, ADMIN
}
