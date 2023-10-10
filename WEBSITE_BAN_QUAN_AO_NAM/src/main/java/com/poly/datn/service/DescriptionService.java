package com.poly.datn.service;

import com.poly.datn.entity.Color;
import com.poly.datn.entity.Description;
import org.springframework.data.domain.Page;

public interface DescriptionService {
    Page<Description> phanTrang(Integer pageNum, Integer pageNo);

    void add(Description description);

    Description detail(Long id);
}
