package com.poly.datn.service;

import com.poly.datn.entity.Size;
import com.poly.datn.util.MessageUtil;

import java.util.List;

public interface SizeService {

    List<Size> getAll();

    Size detail(Integer id);

    MessageUtil save(Size size);
}
