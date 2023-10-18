package com.poly.datn.service.impl;

import com.poly.datn.entity.Color;
import com.poly.datn.repository.ColorRepository;
import com.poly.datn.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {
    @Autowired
    ColorRepository colorRepository;


    @Override
    public List<Color> getAll() {
        return colorRepository.findAll();
    }

    @Override
    public void add(Color color) {
        colorRepository.save(color);

    }

    @Override
    public Color detail(Integer id) {
        return colorRepository.getById(id);
    }
}
