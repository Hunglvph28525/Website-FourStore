package com.poly.datn.service;

import com.poly.datn.entity.Color;

import java.util.List;

public interface ColorService {

    List<Color> getAll();

    void save(Color color);

    Color detail(Integer id);
}
