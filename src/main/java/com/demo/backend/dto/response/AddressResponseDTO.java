package com.demo.backend.dto.response;

import com.demo.backend.model.enums.AddressType;
import lombok.Data;

@Data
public class AddressResponseDTO {
    private Long id;
    private Long userId;
    private AddressType addressType;
    private String fullName;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phone;
    private boolean isDefault;
}
