package com.demo.backend.repository;

import com.demo.backend.model.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for NotificationTemplate entity.
 */
@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    /**
     * Find template by key.
     */
    Optional<NotificationTemplate> findByTemplateKey(String templateKey);

    /**
     * Find active template by key.
     */
    Optional<NotificationTemplate> findByTemplateKeyAndIsActiveTrue(String templateKey);
}
