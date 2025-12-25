package com.demo.backend.repository;

import com.demo.backend.model.Address;
import com.demo.backend.model.User;
import com.demo.backend.model.enums.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
    List<Address> findByUserId(Long userId);
    List<Address> findByAddressType(AddressType type);
}
