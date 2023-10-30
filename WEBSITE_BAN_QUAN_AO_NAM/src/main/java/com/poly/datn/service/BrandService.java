package com.poly.datn.service;

import com.poly.datn.entity.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> getAll();

    void save(Brand color);
}
