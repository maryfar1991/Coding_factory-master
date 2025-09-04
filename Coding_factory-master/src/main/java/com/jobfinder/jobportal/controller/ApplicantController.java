package com.jobfinder.jobportal.controller;

import com.jobfinder.jobportal.entity.Applicant;
import com.jobfinder.jobportal.service.ApplicantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applicants")
public class ApplicantController {

    private final ApplicantService applicantService;

    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @GetMapping
    public List<Applicant> getAllApplicants() {
        return applicantService.getAllApplicants();
    }

    @GetMapping("/{id}")
    public Optional<Applicant> getApplicantById(@PathVariable Long id) {
        return applicantService.getApplicantById(id);
    }

    @PostMapping
    public Applicant createApplicant(@RequestBody Applicant applicant) {
        return applicantService.createApplicant(applicant);
    }

    @PutMapping("/{id}")
    public Applicant updateApplicant(@PathVariable Long id, @RequestBody Applicant applicant) {
        return applicantService.updateApplicant(id, applicant);
    }

    @DeleteMapping("/{id}")
    public void deleteApplicant(@PathVariable Long id) {
        applicantService.deleteApplicant(id);
    }
}
