package com.jobfinder.jobportal.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    // 🔐 Security rules για Spring Security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                //.exceptionHandling(exception -> exception
                        //.accessDeniedHandler(csrfAccessDeniedHandler())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/csrf-token").permitAll()

                        // 🎯 Δημόσια προβολή αγγελιιών για όλους
                        .requestMatchers(HttpMethod.GET, "/api/jobs/**").permitAll()

                        // ✅ Υποβολή αίτησης από APPLICANT
                        .requestMatchers(HttpMethod.POST, "/api/applications/apply/**").hasAuthority("APPLICANT")
                        .requestMatchers(HttpMethod.GET, "/api/applications/me").hasAuthority("APPLICANT")

                        // ✅ Προβολή και χειρισμός αιτήσεων από COMPANY
                        .requestMatchers(HttpMethod.GET, "/api/applications/job/*").hasAuthority("COMPANY")
                        .requestMatchers(HttpMethod.PUT, "/api/applications/*/approve").hasAuthority("COMPANY")
                        .requestMatchers(HttpMethod.DELETE, "/api/applications/*/reject").hasAuthority("COMPANY")

                        // 🛠️ Διαχείριση από εταιρίες
                        .requestMatchers("/api/company/**").hasAuthority("COMPANY")

                        // 🔒 Υπόλοιπα endpoints προστατευμένα
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // ✅ Εδώ φορτώνεται το φίλτρο

        return http.build();
    }


    // 🔐 Κρυπτογράφηση κωδικών με BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔧 RequestContext για σωστή session διαχείριση
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    // 🌐 CORS για frontend επικοινωνία με backend
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("http://localhost:3000") // ✅ αντί για allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    // 🧪 Προαιρετική καταγραφή για CSRF απορρίψεις
    public AccessDeniedHandler csrfAccessDeniedHandler() {
        return (request, response, ex) -> {
            System.out.println("⛔ CSRF block: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF error");
        };
    }
}



//.ignoringRequestMatchers("/api/auth/register") // 👈 Δοκιμαστικά να αγνοείται το CSRF για αυτό
//.ignoringRequestMatchers("/api/auth/register", "/api/auth/login")



