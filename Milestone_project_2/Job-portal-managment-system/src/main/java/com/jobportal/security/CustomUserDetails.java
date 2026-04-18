package com.jobportal.security;

import com.jobportal.entity.Employer;
import com.jobportal.entity.User;
import com.jobportal.enums.RoleEnum;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom UserDetails wrapping either a User or Employer entity.
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final String fullName;
    private final RoleEnum role;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    // Constructor for User (Student/Admin)
    public CustomUserDetails(User user) {
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.fullName = user.getFullName();
        this.role = user.getRole();
        this.active = user.getIsActive();
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    // Constructor for Employer
    public CustomUserDetails(Employer employer) {
        this.id = employer.getEmployerId();
        this.email = employer.getEmail();
        this.password = employer.getPassword();
        this.fullName = employer.getCompanyName();
        this.role = RoleEnum.EMPLOYER;
        this.active = employer.getIsActive();
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_EMPLOYER")
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
