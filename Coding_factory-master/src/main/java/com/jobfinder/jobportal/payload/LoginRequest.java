package com.jobfinder.jobportal.payload;

public class LoginRequest {

    private String email;
    private String password;

    // ✅ Constructor χωρίς παραμέτρους (απαραίτητο για Spring @RequestBody)
    public LoginRequest() {
    }

    // ✅ Constructor με παραμέτρους (προαιρετικό)
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // ✅ Getter για email
    public String getEmail() {
        return email;
    }

    // ✅ Setter για email
    public void setEmail(String email) {
        this.email = email;
    }

    // ✅ Getter για password
    public String getPassword() {
        return password;
    }

    // ✅ Setter για password
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}



