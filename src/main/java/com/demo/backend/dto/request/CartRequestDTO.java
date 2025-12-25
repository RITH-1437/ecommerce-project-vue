package com.demo.backend.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartRequestDTO {
    private Long userId;          // optional (guest checkout)
    private String sessionId;     // for guest users
    private LocalDateTime expiresAt;
}
