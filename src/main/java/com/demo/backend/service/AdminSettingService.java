package com.demo.backend.service;

import com.demo.backend.model.AdminSetting;
import java.util.List;

public interface AdminSettingService {

    AdminSetting set(AdminSetting setting);

    AdminSetting findByKey(String key);

    List<AdminSetting> findByCategory(String category);
}
