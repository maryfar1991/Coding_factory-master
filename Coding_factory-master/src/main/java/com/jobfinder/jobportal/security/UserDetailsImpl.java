package com.jobfinder.jobportal.security;

import com.jobfinder.jobportal.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    // ✅ Ρόλος χρήστη (π.χ. COMPANY ή APPLICANT)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
    }

    // ✅ Χρησιμοποιείται για login — email ως αναγνωριστικό
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // ✅ Κωδικός
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // ✅ Αν είναι ενεργός — προαιρετικά true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // ✨ Επιπλέον πρόσβαση στο entity αν χρειαστεί
    public User getUser() {
        return user;
    }

    public String getRole() {
        return user.getRole();
    }
}
