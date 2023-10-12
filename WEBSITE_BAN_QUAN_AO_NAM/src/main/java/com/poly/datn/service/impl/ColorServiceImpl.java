package com.poly.datn.service.impl;

import com.poly.datn.entity.Category;
import com.poly.datn.entity.Color;
import com.poly.datn.repository.ColorRepository;
import com.poly.datn.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<Color> phanTrang(Integer pageNum, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNum, pageNo);
        return colorRepository.findAll(pageable);
    }

    @Override
    public void add(Color color) {
        colorRepository.save(color);

    }

    @Override
    public Color detail(Integer id) {
        return colorRepository.getById(id);
    }

    @Override
    public void delete(Integer id) {
        colorRepository.deleteById(id);

    }
}
