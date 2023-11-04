package com.poly.datn.service;

import com.poly.datn.entity.TypeProduct;
import com.poly.datn.util.MessageUtil;

import java.util.List;

public interface TypeProductService {

    MessageUtil save(TypeProduct typeProduct);

    Object detail(Long id);

    List<TypeProduct> getAll();
}
