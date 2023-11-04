package com.poly.datn.service.impl;

import com.poly.datn.entity.Color;
import com.poly.datn.repository.ColorRepository;
import com.poly.datn.service.ColorService;
import com.poly.datn.util.MessageUtil;
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
    public MessageUtil save(Color color) {
        if (colorRepository.existsByName(color.getName())) {
            return MessageUtil.builder().status(0).message("Thêm thất bại vì màu sắc đã có !").type("bg-danger").build();
        } else {
            colorRepository.save(color);
            return MessageUtil.builder().status(1).message("Thêm thành công !").type("bg-success").build();
        }

    }

}
