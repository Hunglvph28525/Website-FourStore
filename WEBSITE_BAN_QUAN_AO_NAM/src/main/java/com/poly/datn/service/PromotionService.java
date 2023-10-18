package com.poly.datn.service;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Promotion;
import com.poly.datn.entity.Size;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PromotionService {
    Page<Promotion> phanTrang(Integer pageNum, Integer pageNo);

    void add(PromotionDto dto);

    Promotion detail(Long id);

    List<PromotionDto> getAll();
}
