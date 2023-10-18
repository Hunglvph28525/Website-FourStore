package com.poly.datn.service;

import com.poly.datn.entity.Size;

import java.util.List;

public interface SizeService {

    List<Size> getAll();

    void add(Size size);

    Size detail(Integer id);
}
