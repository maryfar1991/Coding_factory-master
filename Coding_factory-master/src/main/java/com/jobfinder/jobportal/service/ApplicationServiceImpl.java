package com.jobfinder.jobportal.service;

import com.jobfinder.jobportal.entity.Application;
import com.jobfinder.jobportal.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Override
    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    @Override
    public Application createApplication(Application application) {
        return applicationRepository.save(application);
    }

    @Override
    public Application updateApplication(Long id, Application application) {
        if (applicationRepository.existsById(id)) {
            application.setId(id);
            return applicationRepository.save(application);
        }
        return null;
    }

    @Override
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
}
