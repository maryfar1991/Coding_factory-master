package com.jobfinder.jobportal.repository;

import com.jobfinder.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔍 Βρες χρήστη από email (π.χ. auth.getName())
    Optional<User> findByEmail(String email);
}




