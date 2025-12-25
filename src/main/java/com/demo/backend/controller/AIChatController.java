package com.demo.backend.controller;

import com.demo.backend.dto.ai.AIChatRequest;
import com.demo.backend.dto.ai.AIChatResponse;
import com.demo.backend.service.AIChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIChatController {

    private final AIChatService aiChatService;

    @PostMapping("/chat")
    public ResponseEntity<AIChatResponse> chat(@RequestBody AIChatRequest req) {
        return ResponseEntity.ok(aiChatService.process(req));
    }
}
