package com.poly.datn.service.impl;

import com.poly.datn.entity.Promotion;
import com.poly.datn.repository.PromotionRepository;
import com.poly.datn.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    @Override
    public Page<Promotion> phanTrang(Integer pageNum, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNum, pageNo);
        return promotionRepository.findAll(pageable);
    }

    @Override
    public void add(Promotion promotion) {
        promotionRepository.save(promotion);
    }

    @Override
    public Promotion detail(Long id) {
        return promotionRepository.getById(id);
    }
}
