package com.jobfinder.jobportal.service;

import com.jobfinder.jobportal.entity.User;
import com.jobfinder.jobportal.payload.*;
import com.jobfinder.jobportal.repository.UserRepository; // ✅ import UserRepository
import com.jobfinder.jobportal.security.JwtTokenProvider; // ✅ import JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 🔍 Βρες χρήστη από DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("⛔ User not found"));

        // ✅ Έλεγξε κωδικό
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("⛔ Invalid credentials");
        }

        // 🔐 Δημιούργησε JWT
        String token = jwtTokenProvider.generateToken(user.getEmail());

        // ✨ Επιστροφή του token + role
        return new LoginResponse(token, user.getRole()); // υποθέτουμε ότι έχει getRole()
    }


    @Override
    public void register(RegisterRequest request) {
        // check if user exists
        // save user with encoded password
    }
}

