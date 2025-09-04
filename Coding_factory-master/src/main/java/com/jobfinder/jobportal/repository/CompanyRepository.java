package com.jobfinder.jobportal.repository;

import com.jobfinder.jobportal.entity.Company;
import com.jobfinder.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    // 🔍 Βρες εταιρεία με βάση το companyName
    Optional<Company> findByCompanyName(String companyName);

    // 🔍 Αναζήτηση με keyword στο companyName
    List<Company> findByCompanyNameContainingIgnoreCase(String keyword);

    // 🔍 Βρες εταιρεία με βάση τον χρήστη (login μέσω JWT → email → User)
    Optional<Company> findByUser(User user);
}



