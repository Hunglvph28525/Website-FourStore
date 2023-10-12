package com.poly.datn.service;

import com.poly.datn.entity.Category;
import com.poly.datn.entity.Color;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ColorService {

    List<Color> getAll();

    Page<Color> phanTrang(Integer pageNum, Integer pageNo);

    void add(Color color);

    Color detail(Integer id);
    void delete(Integer id);
}
