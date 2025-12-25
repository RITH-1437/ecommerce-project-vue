package com.demo.backend.service.impl;

import com.demo.backend.model.ActivityLog;
import com.demo.backend.repository.ActivityLogRepository;
import com.demo.backend.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository repo;

    @Override
    public ActivityLog log(ActivityLog log) {
        return repo.save(log);
    }

    @Override
    public List<ActivityLog> findByUserId(Long userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public List<ActivityLog> findByEntity(String entityType, Long entityId) {
        return repo.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    public List<ActivityLog> findByAction(String action) {
        return repo.findByAction(action);
    }
}
