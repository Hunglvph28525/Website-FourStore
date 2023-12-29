package com.poly.datn.service;

import com.poly.datn.entity.Address;
import com.poly.datn.util.MessageUtil;

import java.util.List;

public interface AddressService {

    void add(Address address);

    MessageUtil selectMacDinh(Long idUser, Long idAddress);

    List<Address> getAddressForCustomer();
}
