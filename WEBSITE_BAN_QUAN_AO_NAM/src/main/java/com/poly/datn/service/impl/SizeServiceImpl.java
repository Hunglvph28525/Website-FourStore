package com.poly.datn.service.impl;

import com.poly.datn.entity.Size;
import com.poly.datn.repository.SizeRepository;
import com.poly.datn.service.SizeService;
import com.poly.datn.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {
    @Autowired
    SizeRepository sizeRepository;


    @Override
    public List<Size> getAll() {
        return sizeRepository.findAll();
    }


    @Override
    public Size detail(Integer id) {
        return sizeRepository.getById(id);
    }

    @Override
    public MessageUtil save(Size size) {
        if (sizeRepository.existsByName(size.getName())) {
            return MessageUtil.builder().status(0).message("Thêm thất bại vì kích cỡ đã có !").type("bg-danger").build();
        } else {
            sizeRepository.save(size);
            return MessageUtil.builder().status(1).message("Thêm thành công !").type("bg-success").build();
        }
    }
}

