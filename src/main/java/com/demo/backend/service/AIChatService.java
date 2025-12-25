package com.demo.backend.service;

import com.demo.backend.dto.ai.AIChatRequest;
import com.demo.backend.dto.ai.AIChatResponse;

public interface AIChatService {
    AIChatResponse process(AIChatRequest request);
}
