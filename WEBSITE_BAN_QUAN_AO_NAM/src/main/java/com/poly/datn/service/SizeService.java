package com.poly.datn.service;

import com.poly.datn.entity.Description;
import com.poly.datn.entity.Size;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SizeService {
    List<Size> getAll();

    Page<Size> phanTrang(Integer pageNum, Integer pageNo);

    void add(Size size);

    Size detail(Integer id);
    void delete(Integer id);
}
