package com.demo.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    public static Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }

        Object principal = auth.getPrincipal();

        // If your custom UserDetails has getId()
        if (principal instanceof UserDetails) {
            try {
                return (Long) principal.getClass().getMethod("getId").invoke(principal);
            } catch (Exception e) {
                throw new RuntimeException("Your UserDetails class does not have getId() method");
            }
        }

        return null;
    }
}
