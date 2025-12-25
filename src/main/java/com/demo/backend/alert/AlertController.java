package com.demo.backend.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/active")
    public ResponseEntity<List<Alert>> listActive() {
        return ResponseEntity.ok(alertService.listActiveAlerts());
    }

    @PostMapping("/{id}/ack")
    public ResponseEntity<Alert> ack(@PathVariable Long id, @RequestParam(required = false) String by) {
        return ResponseEntity.ok(alertService.acknowledge(id, by == null ? "admin" : by));
    }
}
