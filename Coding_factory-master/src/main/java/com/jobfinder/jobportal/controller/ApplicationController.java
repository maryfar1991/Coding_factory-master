package com.jobfinder.jobportal.controller;

import com.jobfinder.jobportal.entity.Application;
import com.jobfinder.jobportal.entity.Applicant;
import com.jobfinder.jobportal.entity.Company;
import com.jobfinder.jobportal.entity.Job;
import com.jobfinder.jobportal.entity.User;
import com.jobfinder.jobportal.repository.*;
import com.jobfinder.jobportal.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;



    public ApplicationController(ApplicationService applicationService,
                                 ApplicationRepository applicationRepository,
                                 JobRepository jobRepository,
                                 ApplicantRepository applicantRepository, UserRepository userRepository, CompanyRepository companyRepository) {
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.applicantRepository = applicantRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    // 🧾 Όλες οι αιτήσεις
    @GetMapping
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }

    // 🔍 Μία αίτηση βάσει ID
    @GetMapping("/{id}")
    public Optional<Application> getApplicationById(@PathVariable Long id) {
        return applicationService.getApplicationById(id);
    }

    // 📝 Υποβολή αίτησης από χρήστη σε αγγελία
    @PostMapping("/apply/{jobId}")
    public ResponseEntity<String> createApplication(@PathVariable Long jobId,
                                                    @RequestBody Application requestBody,
                                                    Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("❌ Δεν βρέθηκε χρήστης με email: " + email));

        // 🔒 Έλεγχος ότι είναι τύπου APPLICANT
        if (!"APPLICANT".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("🚫 Ο χρήστης δεν έχει ρόλο APPLICANT και δεν μπορεί να υποβάλει αίτηση.");
        }

        Applicant applicant = applicantRepository.findByEmail(email)
                .orElseGet(() -> {
                    Applicant newApplicant = new Applicant();
                    newApplicant.setEmail(email);
                    newApplicant.setUser(user);

                    // Προαιρετικά default values
                    newApplicant.setFirstname("N/A");
                    newApplicant.setLastname("N/A");
                    newApplicant.setPhoneNumber("0000000000");
                    newApplicant.setResume("N/A");
                    newApplicant.setSkills("N/A");

                    System.out.println("🆕 Δημιουργείται νέος applicant για χρήστη με role APPLICANT");
                    return applicantRepository.save(newApplicant);
                });



        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("❌ Δεν βρέθηκε η αγγελία με ID: " + jobId));

        Company company = job.getCompany();

        Application application = new Application();
        application.setApplicant(applicant);
        application.setJob(job);
        application.setCompany(company);
        application.setStatus("PENDING");
        application.setAppliedAt(LocalDateTime.now());
        application.setCoverLetter(requestBody.getCoverLetter());

        applicationRepository.save(application);

        System.out.println("📨 Ο χρήστης " + email + " υπέβαλε αίτηση για τη θέση: " + job.getTitle());
        System.out.println("📬 Ειδοποίηση προς εταιρεία: Νέα αίτηση για τη θέση '" + job.getTitle() + "' από τον χρήστη " + email);

        return ResponseEntity.ok("Η αίτηση υποβλήθηκε με επιτυχία!");
    }

    // ✅ Έγκριση αίτησης
    @PutMapping("/{applicationId}/approve")
    public ResponseEntity<String> approveApplication(@PathVariable Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("❌ Αίτηση δεν βρέθηκε με ID: " + applicationId));

        app.setStatus("APPROVED");
        applicationRepository.save(app);

        System.out.println("✅ Η εταιρεία ενέκρινε την αίτηση του χρήστη " + app.getApplicant().getEmail());
        return ResponseEntity.ok("Αίτηση εγκρίθηκε");
    }

    // ❌ Απόρριψη αίτησης και διαγραφή από τη βάση
    @DeleteMapping("/{applicationId}/reject")
    public ResponseEntity<String> rejectApplication(@PathVariable Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("❌ Αίτηση δεν βρέθηκε με ID: " + applicationId));

        applicationRepository.delete(app);

        System.out.println("❌ Η εταιρεία απέρριψε την αίτηση του χρήστη " + app.getApplicant().getEmail());
        System.out.println("📬 Μήνυμα προς χρήστη: Η αίτησή σας απορρίφθηκε από την εταιρεία.");
        return ResponseEntity.ok("Αίτηση απορρίφθηκε και διαγράφηκε.");
    }

    // 👤 Αιτήσεις για logged-in χρήστη
    @GetMapping("/me")
    public ResponseEntity<List<Application>> getMyApplications(Authentication auth) {
        String email = auth.getName();
        Applicant applicant = applicantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("❌ Δεν βρέθηκε applicant με email: " + email));

        List<Application> myApps = applicationRepository.findByApplicant(applicant);
        System.out.println("🔍 Βρέθηκαν " + myApps.size() + " αιτήσεις για τον χρήστη " + email);
        return ResponseEntity.ok(myApps);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsForJob(@PathVariable Long jobId, Authentication auth) {
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("❌ Δεν βρέθηκε χρήστης"));

        if (!"COMPANY".equalsIgnoreCase(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "🚫 Δεν έχεις δικαίωμα προβολής αιτήσεων.");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("❌ Δεν βρέθηκε η αγγελία"));

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("❌ Δεν βρέθηκε εταιρεία για τον χρήστη"));

        // 💡 Εξασφάλισε ότι η αγγελία ανήκει στην εταιρεία του χρήστη
        if (!job.getCompany().getId().equals(company.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "🚫 Δεν έχεις πρόσβαση σε αυτήν την αγγελία.");
        }

        List<Application> applications = applicationRepository.findByJob(job);
        return ResponseEntity.ok(applications);
    }

}

