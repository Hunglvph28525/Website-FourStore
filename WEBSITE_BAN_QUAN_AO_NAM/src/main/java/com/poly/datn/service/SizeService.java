package com.poly.datn.service;

import com.poly.datn.entity.Size;

import java.util.List;

public interface SizeService {

    List<Size> getAll();

    Size detail(Integer id);

    void save(Size size);
}
