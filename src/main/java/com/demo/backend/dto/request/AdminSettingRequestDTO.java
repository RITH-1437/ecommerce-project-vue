package com.demo.backend.dto.request;

import lombok.Data;

@Data
public class AdminSettingRequestDTO {
    private String settingKey;
    private String settingValue;
    private String settingType;
    private String description;
    private String category;
    private Long updatedBy;
}
