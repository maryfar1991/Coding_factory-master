package com.jobfinder.jobportal.service;

import com.jobfinder.jobportal.entity.Applicant;
import com.jobfinder.jobportal.repository.ApplicantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicantServiceImpl implements ApplicantService {

    private final ApplicantRepository applicantRepository;

    public ApplicantServiceImpl(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    @Override
    public List<Applicant> getAllApplicants() {
        return applicantRepository.findAll();
    }

    @Override
    public Optional<Applicant> getApplicantById(Long id) {
        return applicantRepository.findById(id);
    }

    @Override
    public Applicant createApplicant(Applicant applicant) {
        return applicantRepository.save(applicant);
    }

    @Override
    public Applicant updateApplicant(Long id, Applicant applicant) {
        if (applicantRepository.existsById(id)) {
            applicant.setId(id);
            return applicantRepository.save(applicant);
        }
        return null;
    }

    @Override
    public void deleteApplicant(Long id) {
        applicantRepository.deleteById(id);
    }
}
