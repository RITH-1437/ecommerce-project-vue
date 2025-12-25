package com.demo.backend.dto.request;

import lombok.Data;

@Data
public class ContactMessageRequestDTO {
    private Long userId;
    private String name;
    private String email;
    private String subject;
    private String message;
    private String priority;  // LOW, MEDIUM, HIGH
}
