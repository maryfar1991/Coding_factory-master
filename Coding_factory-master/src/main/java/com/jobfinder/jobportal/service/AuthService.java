package com.jobfinder.jobportal.service;

import com.jobfinder.jobportal.payload.*;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void register(RegisterRequest request);
}

