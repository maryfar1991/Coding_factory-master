package com.jobfinder.jobportal.service;

import com.jobfinder.jobportal.entity.Company;
import com.jobfinder.jobportal.repository.CompanyRepository;
import com.jobfinder.jobportal.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company updateCompany(Long id, Company company) {
        Optional<Company> existingCompany = companyRepository.findById(id);
        if (existingCompany.isPresent()) {
            Company updatedCompany = existingCompany.get();
            updatedCompany.setCompanyName(company.getCompanyName());
            updatedCompany.setWebsite(company.getWebsite());
            updatedCompany.setDescription(company.getDescription());
            updatedCompany.setUser(company.getUser());
            return companyRepository.save(updatedCompany);
        } else {
            throw new RuntimeException("Company not found with id " + id);
        }
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}

