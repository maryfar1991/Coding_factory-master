package com.jobfinder.jobportal.payload;

public class RegisterRequest {
    private String email;
    private String password;
    private String role;

    // 🧱 Default constructor για Spring / Jackson
    public RegisterRequest() {}

    // 🔧 Constructor για χειροκίνητη δημιουργία, αν χρειαστεί
    public RegisterRequest(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // ✏️ Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // 📤 Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}


