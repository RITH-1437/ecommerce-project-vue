package com.demo.backend.service.impl;

import com.demo.backend.exception.BusinessException;
import com.demo.backend.model.AdminSetting;
import com.demo.backend.repository.AdminSettingRepository;
import com.demo.backend.service.AdminSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSettingServiceImpl implements AdminSettingService {

    private final AdminSettingRepository repo;

    @Override
    public AdminSetting set(AdminSetting setting) {
        return repo.save(setting);
    }

    @Override
    public AdminSetting findByKey(String key) {
        return repo.findBySettingKey(key)
                .orElseThrow(() -> new BusinessException("Setting not found"));
    }

    @Override
    public List<AdminSetting> findByCategory(String category) {
        return repo.findByCategory(category);
    }
}
