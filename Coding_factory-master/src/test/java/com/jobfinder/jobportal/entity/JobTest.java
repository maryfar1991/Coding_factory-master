package com.jobfinder.jobportal.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobTest {

    @Test
    void testJobContainsCompanyAfterAssignment() {
        // Δημιουργούμε εικονική εταιρεία
        Company company = new Company();
        company.setId(1L);
        company.setCompanyName("Ergo");
        company.setWebsite("https://ergo.example.com");

        // Δημιουργούμε νέα αγγελία
        Job job = new Job();
        job.setTitle("Java Developer");
        job.setDescription("Αναζητούμε backend developer");
        job.setLocation("Athens");
        job.setSalaryRange("€2000–€3000");

        // ✅ Συνδέουμε την εταιρεία
        job.setCompany(company);

        // 🔍 Τεστ: η εταιρεία δεν είναι null και περιέχει το σωστό όνομα
        assertNotNull(job.getCompany(), "Η εταιρεία δεν έχει συνδεθεί σωστά στο job");
        assertEquals("Ergo", job.getCompany().getCompanyName(), "Το όνομα της εταιρείας δεν ταιριάζει");
        assertEquals(1L, job.getCompany().getId(), "Το ID της εταιρείας δεν ταιριάζει");
    }
}


