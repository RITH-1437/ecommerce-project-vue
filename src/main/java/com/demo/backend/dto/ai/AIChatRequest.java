package com.demo.backend.dto.ai;

import lombok.Data;

import java.util.List;

@Data
public class AIChatRequest {
    private String message;
    private List<String> history; // optional, can be null
}
