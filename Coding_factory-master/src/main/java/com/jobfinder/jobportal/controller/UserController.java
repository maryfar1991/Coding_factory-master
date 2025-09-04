package com.jobfinder.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.jobfinder.jobportal.entity.User;
import com.jobfinder.jobportal.security.JwtTokenProvider;
import com.jobfinder.jobportal.payload.LoginRequest;
import com.jobfinder.jobportal.payload.LoginResponse;
import com.jobfinder.jobportal.payload.RegisterRequest;
import com.jobfinder.jobportal.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }


    // ===============================
    // 🔐 AUTH ENDPOINTS
    // ===============================

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/login", produces = "application/json")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Optional<User> optionalUser = userService.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            System.out.println("🔍 Input password: " + request.getPassword());
            System.out.println("🔍 Stored hash: " + user.getPassword());
            System.out.println("✅ Password matches? " + passwordEncoder.matches(request.getPassword(), user.getPassword()));
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtTokenProvider.generateToken(user.getEmail());
                String role = user.getRole(); // ✅ ορίζουμε το role από το χρήστη

                return new LoginResponse(token, role); // ✅ επιστρέφουμε και τα δύο
            }
        }
        throw new RuntimeException("Invalid email or password");
    }



    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        System.out.println("Request received: " + request.getEmail());
        // 🔎 Καταγραφή εισερχόμενων πεδίων για debugging
        System.out.println("📨 Email: " + request.getEmail());
        System.out.println("📨 Password: " + request.getPassword());
        System.out.println("📨 Role: " + request.getRole());

        // ✅ Έλεγχος για διπλό email
        if (userService.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Το email υπάρχει ήδη");
        }

        // 👤 Δημιουργία νέου χρήστη
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        //newUser.setPassword(request.getPassword()); // 💡 Πρόσθεσε PasswordEncoder για ασφάλεια
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole() != null ? request.getRole() : "USER"); // default σε περίπτωση null

        // 📦 Αποθήκευση
        userService.createUser(newUser);

        // 🎉 Επιτυχές μήνυμα
        System.out.println("🔐 Stored password: " + newUser.getPassword());
        return "Ο χρήστης δημιουργήθηκε με επιτυχία!";


    }

    // ===============================
    // 👤 USER CRUD ENDPOINTS
    // ===============================

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

