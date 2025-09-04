package com.jobfinder.jobportal.service;

import com.jobfinder.jobportal.entity.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    List<Company> getAllCompanies();

    Optional<Company> getCompanyById(Long id);

    Company createCompany(Company company);

    Company updateCompany(Long id, Company company);

    void deleteCompany(Long id);
}
