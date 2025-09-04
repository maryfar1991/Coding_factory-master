package com.jobfinder.jobportal.controller;

import com.jobfinder.jobportal.dto.JobResponse;
import com.jobfinder.jobportal.entity.Company;
import com.jobfinder.jobportal.entity.Job;
import com.jobfinder.jobportal.entity.User;
import com.jobfinder.jobportal.repository.CompanyRepository;
import com.jobfinder.jobportal.repository.JobRepository;
import com.jobfinder.jobportal.repository.UserRepository;
import com.jobfinder.jobportal.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyJobController {

    private final JobService jobService;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;


    public CompanyJobController(JobService jobService, JobRepository jobRepository,
                                UserRepository userRepository,
                                CompanyRepository companyRepository) {
        this.jobService = jobService;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }



    @PostMapping("/jobs")
    public ResponseEntity<Job> createJob(@RequestBody Job job, Authentication auth) {
        String email = auth.getName();
        System.out.println("📨 Job creation request from user: " + email);

        // 🔍 Βρες χρήστη βάσει email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("❌ Δεν βρέθηκε χρήστης για το email: " + email));

        System.out.println("🧾 Χρήστης που έκανε login:");
        System.out.println("🔹 Email: " + user.getEmail());
        System.out.println("🔹 ID: " + user.getId());
        System.out.println("🔹 Role: " + user.getRole());

        // ✳️ Fallback δημιουργία εταιρείας αν δεν υπάρχει (μόνο για COMPANY)
        Company company = companyRepository.findByUser(user)
                .orElseGet(() -> {
                    if (!"COMPANY".equalsIgnoreCase(user.getRole())) {
                        System.out.println("🚫 Ο χρήστης δεν είναι τύπου COMPANY, δεν δημιουργείται εταιρεία.");
                        throw new RuntimeException("Ο χρήστης δεν είναι τύπου COMPANY.");
                    }

                    String companyName = email.contains("@") ? email.substring(0, email.indexOf("@")) : "anonymous";

                    Company newCompany = new Company();
                    newCompany.setCompanyName(companyName);
                    newCompany.setWebsite("https://default.com");
                    newCompany.setDescription("Auto-created company for user: " + email);
                    newCompany.setUser(user);

                    System.out.println("🆕 Δημιουργείται νέα εταιρεία με όνομα: " + companyName);
                    return companyRepository.save(newCompany);
                });

        System.out.println("🏢 Εταιρεία που θα συνδεθεί με την αγγελία: " + company.getCompanyName());

        // ✅ Σύνδεσε την εταιρεία στο αντικείμενο Job
        job.setCompany(company);

        System.out.println("🧪 [Controller] Εταιρεία μετά το setCompany(): " +
                (job.getCompany() != null ? job.getCompany().getCompanyName() : "NULL"));

        // 📄 Πληροφορίες αγγελίας
        System.out.println("📝 Αγγελία που θα αποθηκευτεί:");
        System.out.println("📌 Τίτλος: " + job.getTitle());
        System.out.println("📌 Περιγραφή: " + job.getDescription());
        System.out.println("📌 Τοποθεσία: " + job.getLocation());
        System.out.println("📌 Μισθός: " + job.getSalaryRange());
        System.out.println("🏢 Εταιρεία: " + job.getCompany().getCompanyName());

        // 💾 Αποθήκευση
        Job savedJob = jobService.createJob(job);

        System.out.println("✅ Αγγελία αποθηκεύτηκε με ID: " + savedJob.getId());
        return ResponseEntity.ok(savedJob);
    }



    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponse>> getCompanyJobs(Authentication auth) {
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Company not found for user"));

        List<Job> companyJobs = jobRepository.findByCompany(company);

        List<JobResponse> jobResponses = companyJobs.stream()
                .map(JobResponse::new)
                .toList();

        System.out.println("🔄 Επιστρέφονται " + jobResponses.size() + " αγγελίες για την εταιρεία " + company.getCompanyName());
        return ResponseEntity.ok(jobResponses);
    }

}


