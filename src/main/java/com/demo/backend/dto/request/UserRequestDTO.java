package com.demo.backend.dto.request;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private String phone;
    private String role;
    private Boolean active;
    private String avatarUrl;
    private Boolean emailVerified;
}
