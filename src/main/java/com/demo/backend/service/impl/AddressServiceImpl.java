package com.demo.backend.service.impl;

import com.demo.backend.exception.BusinessException;
import com.demo.backend.model.Address;
import com.demo.backend.repository.AddressRepository;
import com.demo.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public Address create(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address update(Long id, Address data) {
        Address addr = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Address not found"));

        addr.setFullName(data.getFullName());
        addr.setStreet(data.getStreet());
        addr.setCity(data.getCity());
        addr.setState(data.getState());
        addr.setZipCode(data.getZipCode());
        addr.setCountry(data.getCountry());
        addr.setPhone(data.getPhone());
        addr.setAddressType(data.getAddressType());
        addr.setDefault(data.isDefault());

        return addressRepository.save(addr);
    }

    @Override
    public void delete(Long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public Address findById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Address not found"));
    }

    @Override
    public List<Address> findByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }
}
