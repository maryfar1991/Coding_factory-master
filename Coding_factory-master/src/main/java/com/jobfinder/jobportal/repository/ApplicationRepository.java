package com.jobfinder.jobportal.repository;

import com.jobfinder.jobportal.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobfinder.jobportal.entity.Applicant;
import com.jobfinder.jobportal.entity.Company;
import com.jobfinder.jobportal.entity.Job;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // 🔍 Όλες οι αιτήσεις για συγκεκριμένη αγγελία
    List<Application> findByJob(Job job);

    // 👤 Όλες οι αιτήσεις ενός υποψήφιου
    List<Application> findByApplicant(Applicant applicant);

    // 🏢 Αιτήσεις μιας εταιρείας με συγκεκριμένο status (π.χ. μόνο "PENDING")
    List<Application> findByCompanyAndStatus(Company company, String status);

    // ❌ Διαγραφή αίτησης συγκεκριμένου χρήστη σε συγκεκριμένη αγγελία
    void deleteByJobAndApplicant(Job job, Applicant applicant);
}

