package com.jobportal.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Utility methods for accessing the current authenticated user.
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    /**
     * Get the currently logged-in user's CustomUserDetails.
     */
    public static Optional<CustomUserDetails> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            return Optional.of((CustomUserDetails) auth.getPrincipal());
        }
        return Optional.empty();
    }

    /**
     * Get the current user's ID.
     */
    public static Long getCurrentUserId() {
        return getCurrentUser().map(CustomUserDetails::getId).orElse(null);
    }

    /**
     * Get the current user's email.
     */
    public static String getCurrentUserEmail() {
        return getCurrentUser().map(CustomUserDetails::getEmail).orElse(null);
    }

    /**
     * Check if the current user has a specific role.
     */
    public static boolean hasRole(String role) {
        return getCurrentUser()
                .map(u -> u.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_" + role)))
                .orElse(false);
    }

    /**
     * Check if user is authenticated.
     */
    public static boolean isAuthenticated() {
        return getCurrentUser().isPresent();
    }
}
