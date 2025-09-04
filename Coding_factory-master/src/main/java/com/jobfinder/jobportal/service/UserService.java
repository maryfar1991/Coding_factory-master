package com.jobfinder.jobportal.service;

import com.jobfinder.jobportal.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(User user);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

