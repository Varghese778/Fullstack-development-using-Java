package com.jobportal.config;

import com.jobportal.security.CustomAuthenticationFailureHandler;
import com.jobportal.security.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security configuration with session-based authentication.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF enabled for form submissions, disabled for API and H2 console
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/h2-console/**")
            )
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public pages
                .requestMatchers("/", "/login", "/register", "/forgot-password", "/reset-password").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**", "/webjars/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/jobs", "/jobs/search", "/jobs/{id}").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                // Student pages
                .requestMatchers("/dashboard", "/profile/**", "/my-applications/**", "/resumes/**").hasRole("STUDENT")
                .requestMatchers("/apply/**").hasRole("STUDENT")
                .requestMatchers("/recommendations").hasRole("STUDENT")
                // Employer pages
                .requestMatchers("/employer/**", "/jobs/post", "/jobs/*/edit").hasRole("EMPLOYER")
                // Admin pages
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // API endpoints
                .requestMatchers("/api/users/**").hasAnyRole("STUDENT", "ADMIN")
                .requestMatchers("/api/employers/**").hasAnyRole("EMPLOYER", "ADMIN")
                .requestMatchers("/api/jobs/**").authenticated()
                .requestMatchers("/api/applications/**").authenticated()
                .requestMatchers("/api/resumes/**").hasAnyRole("STUDENT", "ADMIN")
                .requestMatchers("/api/notifications/**").authenticated()
                .requestMatchers("/api/analytics/**").authenticated()
                .requestMatchers("/api/recommendations/**").hasRole("STUDENT")
                .requestMatchers("/api/chat/**").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            // Form login
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
            )
            // Logout
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Session management
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/login?expired")
            )
            // Security headers
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin()) // for H2 console
                .contentTypeOptions(content -> {})
                .xssProtection(xss -> {})
            );

        return http.build();
    }
}
