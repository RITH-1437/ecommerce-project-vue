package com.demo.backend.controller;

import com.demo.backend.model.ActivityLog;
import com.demo.backend.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService logService;

    @PostMapping
    public ResponseEntity<ActivityLog> log(@RequestBody ActivityLog log) {
        return ResponseEntity.ok(logService.log(log));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ActivityLog>> getByUser(@PathVariable Long id) {
        return ResponseEntity.ok(logService.findByUserId(id));
    }

    @GetMapping("/entity/{type}/{entityId}")
    public ResponseEntity<List<ActivityLog>> getByEntity(
            @PathVariable String type,
            @PathVariable Long entityId) {
        return ResponseEntity.ok(logService.findByEntity(type, entityId));
    }

    @GetMapping("/action/{action}")
    public ResponseEntity<List<ActivityLog>> getByAction(@PathVariable String action) {
        return ResponseEntity.ok(logService.findByAction(action));
    }
}
