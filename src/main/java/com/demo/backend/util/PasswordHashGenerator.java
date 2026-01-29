package com.demo.backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class to generate BCrypt password hashes
 * Run this class to generate hashed passwords for database insertion
 */
public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Generate hash for admin@123
        String password = "admin@123";
        String hashedPassword = encoder.encode(password);
        
        System.out.println("========================================");
        System.out.println("Password Hash Generator");
        System.out.println("========================================");
        System.out.println("Original Password: " + password);
        System.out.println("BCrypt Hash: " + hashedPassword);
        System.out.println("========================================");
        System.out.println("\nSQL Update Query:");
        System.out.println("UPDATE users SET password_hash = '" + hashedPassword + "' WHERE email = 'admin@applestore.com';");
        System.out.println("========================================");
    }
}
