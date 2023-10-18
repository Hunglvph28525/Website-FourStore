package com.poly.datn.service.impl;

import com.poly.datn.entity.TypeProduct;
import com.poly.datn.repository.TypeProductRepository;
import com.poly.datn.service.TypeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeProductServiceImpl implements TypeProductService {
    @Autowired
    private TypeProductRepository repository;
    @Override
    public List<TypeProduct> getAll() {
        return repository.findAll();
    }
}
