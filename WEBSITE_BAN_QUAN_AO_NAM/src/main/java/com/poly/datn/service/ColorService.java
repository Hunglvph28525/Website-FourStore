package com.poly.datn.service;

import com.poly.datn.entity.Color;
import com.poly.datn.util.MessageUtil;

import java.util.List;

public interface ColorService {

    List<Color> getAll();

    MessageUtil save(Color color);

}
