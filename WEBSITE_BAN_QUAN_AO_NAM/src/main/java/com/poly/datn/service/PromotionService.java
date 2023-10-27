package com.poly.datn.service;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.Promotion;
import org.springframework.data.domain.Page;

import java.util.List;


public interface PromotionService {
    Page<Promotion> phanTrang(Integer pageNum, Integer pageNo);

    void add(PromotionDto dto);

    Promotion detail(Long id);

    List<PromotionDto> getAll();

    void updateP(PromotionDto dto, Long id);

    void addProduct(List<Product> list, Long id);


    void delete(Long id);
}
