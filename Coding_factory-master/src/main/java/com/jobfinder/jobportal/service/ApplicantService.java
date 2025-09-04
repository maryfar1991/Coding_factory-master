package com.jobfinder.jobportal.service;

import com.jobfinder.jobportal.entity.Applicant;

import java.util.List;
import java.util.Optional;

public interface ApplicantService {

    List<Applicant> getAllApplicants();

    Optional<Applicant> getApplicantById(Long id);

    Applicant createApplicant(Applicant applicant);

    Applicant updateApplicant(Long id, Applicant applicant);

    void deleteApplicant(Long id);
}

