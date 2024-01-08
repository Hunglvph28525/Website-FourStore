package com.poly.datn.service.impl;

import com.poly.datn.entity.Address;
import com.poly.datn.entity.User;
import com.poly.datn.repository.AddressRepository;
import com.poly.datn.repository.UserRepository;
import com.poly.datn.service.AddressService;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository repository;

    @Override
    public void add(Address address) {
        addressRepository.save(address);
    }

    @Override
    public MessageUtil address(Address address) {
        User user = UserUltil.getUser();
        if (user == null) return MessageUtil.builder().status(0).message("Lỗi vui lòng đăng nhập lại").status(1).type("bg-success").build();
        address.setUser(user);
        if(address.getStatus().equals("on")){
            List<Address> addresses = addressRepository.findByIdKhachHang(user.getId());
            addresses.stream().forEach(x -> x.setStatus("off"));
            addressRepository.saveAll(addresses);
        }
        addressRepository.save(address);
        return MessageUtil.builder().status(1).message("thành công").status(1).type("bg-success").build();
    }

    @Override
    public MessageUtil selectMacDinh(Long idUser, Long idAddress) {
        User user = repository.getReferenceById(idUser);
        user.getAddresses().stream().forEach(x ->{
            if (x.getId() == idAddress){
                x.setStatus("on");
            }else {
                x.setStatus("off");
            }
        });
        addressRepository.saveAll(user.getAddresses());
        return MessageUtil.builder().message("thành công").status(1).type("bg-success").build();
    }

    @Override
    public List<Address> getAddressForCustomer() {
        User user = UserUltil.getUser();
        if (user == null)return null;
        List<Address> addresses = repository.getAddressByUser(user.getId());
        return addresses;
    }

    @Override
    public MessageUtil delete(Long id) {
        User user = UserUltil.getUser();
        if (user == null) return MessageUtil.builder().status(0).message("Lỗi vui lòng đăng nhập lại").status(1).type("bg-success").build();
        addressRepository.deleteById(id);
        return MessageUtil.builder().status(1).message("thành công").status(1).type("bg-success").build();
    }
}
