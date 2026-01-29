package com.demo.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA Configuration
 * 
 * Configures JPA/Hibernate settings for the application:
 * - Enables JPA Auditing for automatic timestamp management
 * - Enables Transaction Management
 * - Configures JPA Repositories
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
        "com.demo.backend.repository",
        "com.demo.backend.voucher.repository",
        "com.demo.backend.alert"
})
public class JpaConfig {
    // JPA configuration is handled through application.properties
    // This class enables additional JPA features
}
