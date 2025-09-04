package com.jobfinder.jobportal.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfTokenController {

    @GetMapping("/api/csrf-token")
    public CsrfToken csrf(CsrfToken token, HttpServletRequest request) {
        request.getSession(); // ✅ Εξαναγκασμός session → δημιουργεί JSESSIONID
        return token;
    }
}

