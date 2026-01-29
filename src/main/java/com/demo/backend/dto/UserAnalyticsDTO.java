package com.demo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnalyticsDTO {
    private Long totalUsers;
    private Long activeUsers;
    private Long newUsersThisMonth;
    private Double retentionRate;
    private List<UserRegistration> registrationTrend;
    private List<UserActivity> activityByRole;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRegistration {
        private LocalDate date;
        private Long count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserActivity {
        private String role;
        private Long userCount;
        private Long activeCount;
        private Double activityRate;
    }
}
