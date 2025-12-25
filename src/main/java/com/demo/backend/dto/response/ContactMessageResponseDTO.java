package com.demo.backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContactMessageResponseDTO {

    private Long id;

    private Long userId;

    private String name;
    private String email;

    private String subject;
    private String message;

    private String status;
    private String priority;

    private String adminReply;
    private Long repliedBy;
    private LocalDateTime repliedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
