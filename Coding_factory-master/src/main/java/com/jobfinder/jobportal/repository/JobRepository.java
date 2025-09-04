package com.jobfinder.jobportal.repository;

import com.jobfinder.jobportal.entity.Company;
import com.jobfinder.jobportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    // ✅ Προσθήκη query για αγγελίες συγκεκριμένης εταιρείας
    List<Job> findByCompany(Company company);
}



