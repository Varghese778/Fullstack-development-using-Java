package com.jobportal.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.dto.ai.JobRecommendation;
import com.jobportal.dto.ai.ResumeProfile;
import com.jobportal.dto.ai.WeightedSkill;
import com.jobportal.entity.Job;
import com.jobportal.entity.Resume;
import com.jobportal.enums.JobStatusEnum;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class JobRecommendationService {

    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;
    private final AsiOneService asiOneService;
    private final ObjectMapper objectMapper;

    public List<JobRecommendation> getRecommendations(Long userId, int limit) {
        ResumeProfile profile = getUserProfile(userId);
        if (profile == null || profile.getSkills() == null || profile.getSkills().isEmpty()) {
            return Collections.emptyList();
        }
        List<Job> activeJobs = jobRepository.findByStatus(JobStatusEnum.ACTIVE, PageRequest.of(0, 200)).getContent();
        if (activeJobs.isEmpty()) return Collections.emptyList();

        return activeJobs.stream()
                .map(job -> scoreJob(profile, job))
                .filter(rec -> rec.getRelevanceScore() > 0.1)
                .sorted(Comparator.comparingDouble(JobRecommendation::getRelevanceScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public String getExplanation(Long userId, Long jobId) {
        ResumeProfile profile = getUserProfile(userId);
        Job job = jobRepository.findById(jobId).orElse(null);
        if (profile == null || job == null) return "Please upload and parse your resume first.";

        if (!asiOneService.isAvailable()) {
            return buildSimpleExplanation(scoreJob(profile, job));
        }

        String prompt = "You are a career advisor. Explain in 2-3 concise sentences why this job is a good match. No markdown. Be conversational.";
        String context = String.format("Candidate Skills: %s\nExperience: %s\nJob: %s at %s\nRequired: %s",
                profile.getSkills().stream().map(WeightedSkill::getName).collect(Collectors.joining(", ")),
                profile.getExperienceLevel(),
                job.getJobTitle(), job.getCompanyName(),
                job.getSkillsRequired() != null ? job.getSkillsRequired() : "Not specified");

        String explanation = asiOneService.chatCompletion(prompt, context);
        return explanation != null ? explanation : buildSimpleExplanation(scoreJob(profile, job));
    }

    private JobRecommendation scoreJob(ResumeProfile profile, Job job) {
        Set<String> jobSkillsLower = extractJobSkills(job);
        Map<String, Double> userSkillWeights = profile.getSkills().stream()
                .collect(Collectors.toMap(s -> s.getName().toLowerCase(), WeightedSkill::getWeight, Math::max));

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();
        double totalMatchWeight = 0.0;

        for (String jobSkill : jobSkillsLower) {
            boolean found = false;
            for (var entry : userSkillWeights.entrySet()) {
                if (skillsMatch(entry.getKey(), jobSkill)) {
                    matchedSkills.add(capitalize(jobSkill));
                    totalMatchWeight += entry.getValue();
                    found = true;
                    break;
                }
            }
            if (!found) missingSkills.add(capitalize(jobSkill));
        }

        double skillScore = jobSkillsLower.isEmpty() ? 0.3 : (totalMatchWeight / Math.max(jobSkillsLower.size(), 1)) * 0.6;
        double expScore = calcExpScore(profile.getExperienceLevel(), job);
        double keywordScore = calcKeywordScore(profile, job);
        double roleScore = calcRoleScore(profile, job);
        double totalScore = Math.min(1.0, skillScore + expScore + keywordScore + roleScore);

        return JobRecommendation.builder()
                .jobId(job.getJobId()).jobTitle(job.getJobTitle())
                .companyName(job.getCompanyName()).companyLogo(job.getCompanyLogo())
                .department(job.getDepartment())
                .employmentType(job.getEmploymentType() != null ? job.getEmploymentType().name() : null)
                .experienceLevel(job.getExperienceLevel() != null ? job.getExperienceLevel().name() : null)
                .salaryMin(job.getSalaryMin()).salaryMax(job.getSalaryMax())
                .remotePolicy(job.getRemotePolicy() != null ? job.getRemotePolicy().name() : null)
                .jobDescription(job.getJobDescription())
                .viewCount(job.getViewCount())
                .relevanceScore(totalScore).matchPercentage((int) Math.round(totalScore * 100))
                .matchedSkills(matchedSkills).missingSkills(missingSkills)
                .build();
    }

    private Set<String> extractJobSkills(Job job) {
        Set<String> skills = new HashSet<>();
        if (job.getSkillsRequired() != null) {
            for (String s : job.getSkillsRequired().split("[,;|]")) {
                String t = s.trim().toLowerCase();
                if (!t.isEmpty()) skills.add(t);
            }
        }
        if (job.getRequiredSkills() != null) {
            job.getRequiredSkills().forEach(js -> skills.add(js.getSkillName().toLowerCase()));
        }
        return skills;
    }

    private boolean skillsMatch(String u, String j) {
        if (u.equals(j) || u.contains(j) || j.contains(u)) return true;
        Map<String, List<String>> aliases = Map.of(
                "javascript", List.of("js", "es6"), "typescript", List.of("ts"),
                "kubernetes", List.of("k8s"), "react", List.of("reactjs", "react.js"),
                "node", List.of("nodejs", "node.js"), "python", List.of("py"),
                "amazon web services", List.of("aws"));
        for (var e : aliases.entrySet()) {
            boolean um = u.equals(e.getKey()) || e.getValue().contains(u);
            boolean jm = j.equals(e.getKey()) || e.getValue().contains(j);
            if (um && jm) return true;
        }
        return false;
    }

    private double calcExpScore(String userLevel, Job job) {
        if (userLevel == null || job.getExperienceLevel() == null) return 0.1;
        Map<String, Integer> m = Map.of("FRESHER",0,"JUNIOR",1,"MID",2,"SENIOR",3,"LEAD",4,
                "ZERO_TO_ONE_YEAR",0,"ONE_TO_TWO_YEARS",1,"THREE_TO_FIVE_YEARS",2,"FIVE_PLUS_YEARS",3);
        int diff = Math.abs(m.getOrDefault(userLevel.toUpperCase(),1) - m.getOrDefault(job.getExperienceLevel().name(),1));
        return switch(diff) { case 0->0.2; case 1->0.12; case 2->0.05; default->0.02; };
    }

    private double calcKeywordScore(ResumeProfile p, Job j) {
        if (p.getKeywords()==null||p.getKeywords().isEmpty()) return 0.0;
        String desc = (j.getJobDescription()+" "+j.getJobTitle()).toLowerCase();
        long matches = p.getKeywords().stream().filter(k->desc.contains(k.toLowerCase())).count();
        return Math.min(0.1, (matches/(double)p.getKeywords().size())*0.1);
    }

    private double calcRoleScore(ResumeProfile p, Job j) {
        if (p.getPreferredRoles()==null||p.getPreferredRoles().isEmpty()) return 0.0;
        String t = j.getJobTitle().toLowerCase();
        return p.getPreferredRoles().stream().anyMatch(r->t.contains(r.toLowerCase())||r.toLowerCase().contains(t)) ? 0.1 : 0.0;
    }

    public ResumeProfile getUserProfile(Long userId) {
        Optional<Resume> pr = resumeRepository.findByUserUserIdAndIsPrimary(userId, true);
        if (pr.isEmpty()) {
            List<Resume> resumes = resumeRepository.findByUserUserIdAndIsActiveOrderByUploadedDateDesc(userId, true);
            if (resumes.isEmpty()) return null;
            pr = Optional.of(resumes.get(0));
        }
        Resume r = pr.get();
        if (r.getParsedProfileJson()==null||!Boolean.TRUE.equals(r.getIsAiParsed())) return null;
        try { return objectMapper.readValue(r.getParsedProfileJson(), ResumeProfile.class); }
        catch (Exception e) { log.error("Failed to deserialize profile: {}", e.getMessage()); return null; }
    }

    private String buildSimpleExplanation(JobRecommendation rec) {
        StringBuilder sb = new StringBuilder();
        if (!rec.getMatchedSkills().isEmpty()) sb.append("Your skills in ").append(String.join(", ", rec.getMatchedSkills())).append(" match this role. ");
        if (!rec.getMissingSkills().isEmpty()) sb.append("Consider developing: ").append(String.join(", ", rec.getMissingSkills())).append(". ");
        sb.append("Overall match: ").append(rec.getMatchPercentage()).append("%.");
        return sb.toString();
    }

    private String capitalize(String s) { return s==null||s.isEmpty() ? s : s.substring(0,1).toUpperCase()+s.substring(1); }
}
