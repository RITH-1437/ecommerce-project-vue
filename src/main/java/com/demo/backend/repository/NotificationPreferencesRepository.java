package com.demo.backend.repository;

import com.demo.backend.model.NotificationPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for NotificationPreferences entity.
 */
@Repository
public interface NotificationPreferencesRepository extends JpaRepository<NotificationPreferences, Long> {

    /**
     * Find notification preferences by user ID.
     */
    Optional<NotificationPreferences> findByUserId(Long userId);

    /**
     * Check if preferences exist for user.
     */
    boolean existsByUserId(Long userId);
}
