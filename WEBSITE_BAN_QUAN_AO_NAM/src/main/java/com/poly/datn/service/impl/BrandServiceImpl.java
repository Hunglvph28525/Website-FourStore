package com.poly.datn.service.impl;

import com.poly.datn.entity.Brand;
import com.poly.datn.repository.BrandRepository;
import com.poly.datn.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    BrandRepository brandRepository;

    @Override
    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    @Override
    public void save(Brand brand) {
        brandRepository.save(brand);
    }
}
