package com.poly.datn.service.impl;

import com.poly.datn.entity.Address;
import com.poly.datn.repository.AddressRepository;
import com.poly.datn.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressRepository addressRepository;
    @Override
    public void add(Address address) {
        addressRepository.save(address);
    }
}
