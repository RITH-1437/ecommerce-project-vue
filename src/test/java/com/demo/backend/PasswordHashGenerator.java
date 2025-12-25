package com.demo.backend;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility to generate BCrypt password hashes for seed data
 * Run this class to generate hashes for migration files
 */
public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String adminPassword = "admin@12345";
        String userPassword = "user@123345";

        String adminHash = encoder.encode(adminPassword);
        String userHash = encoder.encode(userPassword);

        System.out.println("=== BCrypt Password Hashes ===");
        System.out.println("\nAdmin Password: " + adminPassword);
        System.out.println("Admin Hash: " + adminHash);
        System.out.println("\nUser Password: " + userPassword);
        System.out.println("User Hash: " + userHash);
        System.out.println("\n=== Copy these hashes to your migration file ===");
    }
}

