package com.demo.backend.repository;

import com.demo.backend.model.AdminSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface AdminSettingRepository extends JpaRepository<AdminSetting, Long> {
    Optional<AdminSetting> findBySettingKey(String key);
    List<AdminSetting> findByCategory(String category);
}
