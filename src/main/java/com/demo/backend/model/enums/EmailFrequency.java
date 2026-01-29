package com.demo.backend.model.enums;

/**
 * Enum representing email notification frequency preferences.
 */
public enum EmailFrequency {
    INSTANT, // Send emails immediately
    DAILY, // Batch and send once per day
    WEEKLY, // Batch and send once per week
    NEVER // Never send email notifications
}
