package com.demo.backend.repository;

import com.demo.backend.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByUserId(Long userId);

    List<ActivityLog> findByEntityTypeAndEntityId(String type, Long id);

    List<ActivityLog> findByAction(String action);
}
