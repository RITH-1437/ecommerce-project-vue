package com.demo.backend.service;

import com.demo.backend.model.Address;
import java.util.List;

public interface AddressService {

    Address create(Address address);

    Address update(Long id, Address address);

    void delete(Long id);

    Address findById(Long id);

    List<Address> findByUserId(Long userId);
}
