package com.demo.backend.dto.request;

import lombok.Data;
import java.util.Map;

@Data
public class ActivityLogRequestDTO {

    private Long userId;

    private String action;

    private String entityType;
    private Long entityId;

    private Map<String, Object> oldValues;
    private Map<String, Object> newValues;

    private String ipAddress;
    private String userAgent;
}
