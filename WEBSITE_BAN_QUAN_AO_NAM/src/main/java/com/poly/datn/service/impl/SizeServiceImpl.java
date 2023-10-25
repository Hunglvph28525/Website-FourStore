package com.poly.datn.service.impl;

import com.poly.datn.entity.Size;
import com.poly.datn.repository.SizeRepository;
import com.poly.datn.service.SizeService;
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
    public void save(Size size) {
        sizeRepository.save(size);
    }
}
