package com.jobportal.security;

import com.jobportal.entity.Employer;
import com.jobportal.entity.User;
import com.jobportal.enums.RoleEnum;
import com.jobportal.repository.EmployerRepository;
import com.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Loads user details from both User and Employer tables for Spring Security.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try to find as Student/Admin user first
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.getIsActive()) {
                throw new UsernameNotFoundException("Account is deactivated");
            }
            if (user.getIsAccountLocked()) {
                throw new UsernameNotFoundException("Account is locked");
            }
            return new CustomUserDetails(user);
        }

        // Try as Employer
        Optional<Employer> employerOpt = employerRepository.findByEmail(email);
        if (employerOpt.isPresent()) {
            Employer employer = employerOpt.get();
            if (!employer.getIsActive()) {
                throw new UsernameNotFoundException("Account is deactivated");
            }
            if (employer.getIsAccountLocked()) {
                throw new UsernameNotFoundException("Account is locked");
            }
            return new CustomUserDetails(employer);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
