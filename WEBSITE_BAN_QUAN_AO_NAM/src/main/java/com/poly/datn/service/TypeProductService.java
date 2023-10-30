package com.poly.datn.service;

import com.poly.datn.entity.TypeProduct;

import java.util.List;

public interface TypeProductService {

    void save(TypeProduct typeProduct);

    Object detail(Long id);

    List<TypeProduct> getAll();
}
