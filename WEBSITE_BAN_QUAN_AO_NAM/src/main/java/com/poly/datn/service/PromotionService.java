package com.poly.datn.service;

import com.poly.datn.entity.Promotion;
import com.poly.datn.entity.Size;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PromotionService {

    List<Promotion> getAll();
    Page<Promotion> phanTrang(Integer pageNum, Integer pageNo);

    void add(Promotion promotion);

    Promotion detail(Long id);

    void delete(Long id);
}
