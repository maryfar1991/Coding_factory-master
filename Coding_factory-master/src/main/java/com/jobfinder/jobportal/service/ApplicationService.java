package com.jobfinder.jobportal.service;

import com.jobfinder.jobportal.entity.Application;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {

    List<Application> getAllApplications();

    Optional<Application> getApplicationById(Long id);

    Application createApplication(Application application);

    Application updateApplication(Long id, Application application);

    void deleteApplication(Long id);
}
