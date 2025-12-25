package com.demo.backend.controller;

import com.demo.backend.model.AdminSetting;
import com.demo.backend.service.AdminSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/settings")
@RequiredArgsConstructor
public class AdminSettingController {

    private final AdminSettingService settingService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AdminSetting> create(@RequestBody AdminSetting setting) {
        return ResponseEntity.ok(settingService.set(setting));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{key}")
    public ResponseEntity<AdminSetting> getByKey(@PathVariable String key) {
        return ResponseEntity.ok(settingService.findByKey(key));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<AdminSetting>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(settingService.findByCategory(category));
    }
}
