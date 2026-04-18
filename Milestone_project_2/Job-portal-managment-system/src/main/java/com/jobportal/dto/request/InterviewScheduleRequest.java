package com.jobportal.dto.request;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InterviewScheduleRequest {
    private LocalDate interviewDate;
    private LocalTime interviewTime;
    private String interviewType; // PHONE, VIDEO, IN_PERSON
    private String interviewLocation;
    private String videoCallLink;
    private String panelMembers;
    private String notes;
}
