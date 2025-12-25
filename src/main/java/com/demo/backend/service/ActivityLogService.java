package com.demo.backend.service;

import com.demo.backend.model.ActivityLog;
import java.util.List;

public interface ActivityLogService {

    ActivityLog log(ActivityLog log);

    List<ActivityLog> findByUserId(Long userId);

    List<ActivityLog> findByEntity(String entityType, Long entityId);

    List<ActivityLog> findByAction(String action);
}
