package com.demo.backend.dto.request;

import lombok.Data;

@Data
public class AddressRequestDTO {
    private Long userId;
    private String addressType;
    private String fullName;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phone;
    private boolean isDefault;
}
