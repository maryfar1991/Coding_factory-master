package com.jobfinder.jobportal.dto;

import com.jobfinder.jobportal.entity.Job;

public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String salaryRange;
    private String companyName;

    public JobResponse(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.location = job.getLocation();
        this.salaryRange = job.getSalaryRange();

        // ✅ Ανάκτηση companyName από email χρήστη της εταιρείας
        if (job.getCompany() != null && job.getCompany().getUser() != null) {
            String email = job.getCompany().getUser().getEmail();
            this.companyName = extractNameFromEmail(email);
        } else {
            this.companyName = "Χωρίς εταιρεία";
        }
    }

    private String extractNameFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "Άγνωστο";
        }
        return email.substring(0, email.indexOf("@"));
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getSalaryRange() { return salaryRange; }
    public String getCompanyName() { return companyName; }
}



