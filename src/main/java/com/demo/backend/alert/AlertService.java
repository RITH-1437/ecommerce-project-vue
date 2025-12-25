package com.demo.backend.alert;

import java.util.List;
import java.util.Map;

public interface AlertService {
    Alert createAlert(AlertType type, AlertSeverity severity, String message, Map<String, Object> metadata);
    List<Alert> listActiveAlerts();
    Alert acknowledge(Long id, String by);
}
