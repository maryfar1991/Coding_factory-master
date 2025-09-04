package com.jobfinder.jobportal.service;

import com.jobfinder.jobportal.entity.Job;
import com.jobfinder.jobportal.repository.JobRepository;
import com.jobfinder.jobportal.service.JobService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceimpl implements JobService {

    private final JobRepository jobRepository;

    public JobServiceimpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    @Override
    public Job createJob(Job job) {
        System.out.println("📥 [Service] Λήψη αίτησης δημιουργίας αγγελίας...");

        if (job == null) {
            System.out.println("❌ Job αντικείμενο είναι null!");
            throw new RuntimeException("Job object is null");
        }

        System.out.println("🔧 [Πριν ορισμό ημερομηνιών]");
        System.out.println("📌 Τίτλος: " + job.getTitle());
        System.out.println("📌 Περιγραφή: " + job.getDescription());
        System.out.println("📌 Τοποθεσία: " + job.getLocation());
        System.out.println("📌 Μισθός: " + job.getSalaryRange());

        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        System.out.println("🕒 Ημερομηνία δημιουργίας: " + job.getCreatedAt());
        System.out.println("🕒 Ημερομηνία ενημέρωσης: " + job.getUpdatedAt());

        System.out.println("🔍 Έλεγχος σύνδεσης με εταιρεία...");
        if (job.getCompany() != null) {
            System.out.println("✅ Εταιρεία συνδεδεμένη: " + job.getCompany().getCompanyName());
            System.out.println("🆔 ID Εταιρείας: " + job.getCompany().getId());
            System.out.println("🌐 Website Εταιρείας: " + job.getCompany().getWebsite());
        } else {
            System.out.println("⚠️ Δεν υπάρχει σύνδεση εταιρείας (company == null)");
        }

        Job saved = jobRepository.save(job);

        System.out.println("💾 Αποθήκευση στην βάση δεδομένων ολοκληρώθηκε.");
        System.out.println("📦 Επιστρεφόμενη αγγελία:");
        System.out.println("🆔 ID: " + saved.getId());
        System.out.println("📌 Τίτλος: " + saved.getTitle());
        System.out.println("🏢 Εταιρεία: " + (saved.getCompany() != null ? saved.getCompany().getCompanyName() : "Χωρίς σύνδεση"));

        return saved;
    }


    @Override
    public Job updateJob(Long id, Job job) {
        Optional<Job> existingJob = jobRepository.findById(id);
        if (existingJob.isPresent()) {
            Job updatedJob = existingJob.get();
            updatedJob.setTitle(job.getTitle());
            updatedJob.setDescription(job.getDescription());
            updatedJob.setLocation(job.getLocation());
            updatedJob.setSalaryRange(job.getSalaryRange());
            updatedJob.setUpdatedAt(LocalDateTime.now());
            updatedJob.setCompany(job.getCompany());
            return jobRepository.save(updatedJob);
        } else {
            throw new RuntimeException("Job not found with id " + id);
        }
    }

    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}

