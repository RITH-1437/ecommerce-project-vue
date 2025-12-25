package com.demo.backend.controller;

import com.demo.backend.model.ContactMessage;
import com.demo.backend.model.enums.ContactStatus;
import com.demo.backend.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService messageService;

    @PostMapping
    public ResponseEntity<ContactMessage> create(@RequestBody ContactMessage message) {
        return ResponseEntity.ok(messageService.create(message));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ContactMessage>> findByUser(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.findByUserId(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ContactMessage>> findByStatus(@PathVariable ContactStatus status) {
        return ResponseEntity.ok(messageService.findByStatus(status));
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<ContactMessage> updateStatus(
            @PathVariable Long id,
            @PathVariable ContactStatus status
    ) {
        return ResponseEntity.ok(messageService.updateStatus(id, status));
    }
}
