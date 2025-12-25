package com.demo.backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityLogResponseDTO {

    private Long id;

    private Long userId;

    private String action;

    private String entityType;
    private Long entityId;

    private Map<String, Object> oldValues;
    private Map<String, Object> newValues;

    private String ipAddress;
    private String userAgent;

    private LocalDateTime createdAt;
}
