package com.demo.backend.mapper;

import com.demo.backend.dto.request.UserRequestDTO;
import com.demo.backend.dto.response.UserResponseDTO;
import com.demo.backend.model.User;
import com.demo.backend.model.enums.UserRole;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "googleId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "totalOrders", ignore = true)
    User toEntity(UserRequestDTO dto);

    @Mapping(source = "role", target = "role", qualifiedByName = "enumToString")
    UserResponseDTO toResponse(User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "googleId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "totalOrders", ignore = true)
    void updateFromDto(UserRequestDTO dto, @MappingTarget User entity);

    @Named("enumToString")
    default String enumToString(UserRole role) {
        return role != null ? role.name() : null;
    }

    @Named("stringToEnum")
    default UserRole stringToEnum(String role) {
        if (role == null) return null;
        try {
            return UserRole.valueOf(role);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
