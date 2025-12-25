package com.demo.backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminSettingResponseDTO {
    private Long id;
    private String settingKey;
    private String settingValue;
    private String settingType;
    private String description;
    private String category;

    private Long updatedBy;
    private LocalDateTime updatedAt;
}
