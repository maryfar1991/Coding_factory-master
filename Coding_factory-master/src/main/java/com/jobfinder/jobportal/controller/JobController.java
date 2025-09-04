package com.jobfinder.jobportal.controller;

import com.jobfinder.jobportal.entity.Company;
import com.jobfinder.jobportal.entity.Job;
import com.jobfinder.jobportal.entity.User;
import com.jobfinder.jobportal.repository.CompanyRepository;
import com.jobfinder.jobportal.repository.UserRepository;
import com.jobfinder.jobportal.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.jobfinder.jobportal.dto.JobResponse;
import com.jobfinder.jobportal.dto.JobRequest;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
//@CrossOrigin(origins = "*") // Προσωρινά για development με React
public class JobController {

    private final JobService jobService;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public JobController(JobService jobService, CompanyRepository companyRepository, UserRepository userRepository) {
        this.jobService = jobService;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> jobDtos = jobService.getAllJobs().stream()
                .map(JobResponse::new)
                .toList();

        System.out.println("🔄 Επιστρέφονται " + jobDtos.size() + " αγγελίες προς το frontend.");
        jobDtos.forEach(dto -> System.out.println("📝 ID: " + dto.getId() + ", Τίτλος: " + dto.getTitle() + ", Εταιρεία: " + dto.getCompanyName()));

        return ResponseEntity.ok(jobDtos);
    }


    // Get job by id
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody JobRequest jobRequest, Authentication auth) {
        String email = auth.getName();
        System.out.println("📨 Δημιουργία αγγελίας από χρήστη: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

        System.out.println("🧾 Χρήστης που έκανε login:");
        System.out.println("🔹 Email: " + user.getEmail());
        System.out.println("🔹 ID: " + user.getId()); // ✅ Αυτό είναι κρίσιμο για τη συσχέτιση με την εταιρεία


        System.out.println("🧪 Έλεγχος εταιρείας από χρήστη με email: " + email);

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> {
                    System.out.println("❌ Δεν βρέθηκε εταιρεία για τον χρήστη με email: " + email);
                    return new RuntimeException("Company not found for user: " + email);
                });


        System.out.println("🏢 Εταιρεία που δημιουργεί την αγγελία: " + company.getCompanyName());

        Job job = new Job();
        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setLocation(jobRequest.getLocation());
        job.setSalaryRange(jobRequest.getSalaryRange());
        job.setCompany(company);

        System.out.println("🧪 [Controller] Company μετά το setCompany(): " +
                (job.getCompany() != null ? job.getCompany().getCompanyName() : "NULL"));


        System.out.println("📝 Αγγελία που θα αποθηκευτεί:");
        System.out.println("📌 Τίτλος: " + job.getTitle());
        System.out.println("📌 Τοποθεσία: " + job.getLocation());
        System.out.println("📌 Μισθός: " + job.getSalaryRange());
        System.out.println("📌 Περιγραφή: " + job.getDescription());
        System.out.println("🏢 Εταιρεία: " + job.getCompany().getCompanyName());

        Job createdJob = jobService.createJob(job);
        System.out.println("✅ Αγγελία αποθηκεύτηκε με ID: " + createdJob.getId());

        return ResponseEntity.ok(createdJob);
    }

    // Update job
    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job) {
        Job updatedJob = jobService.updateJob(id, job);
        return ResponseEntity.ok(updatedJob);
    }

    // Delete job
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}

