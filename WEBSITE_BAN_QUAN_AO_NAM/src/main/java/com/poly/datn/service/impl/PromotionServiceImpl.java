package com.poly.datn.service.impl;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.Promotion;
import com.poly.datn.repository.ProductRepository;
import com.poly.datn.repository.PromotionRepository;
import com.poly.datn.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public Page<Promotion> phanTrang(Integer pageNum, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNum, pageNo);
        return promotionRepository.findAll(pageable);
    }

    @Override
    public void add(PromotionDto dto) {
        promotionRepository.save(dto.promotion());
    }

    @Override
    public Promotion detail(Long id) {
        return promotionRepository.getById(id);
    }

    @Override
    public List<PromotionDto> getAll() {
//        List<PromotionDto> list = new ArrayList<>();
//        List<Promotion> promotions = promotionRepository.findAll();
//        promotions.stream().forEach(promotion -> list.add(new PromotionDto(promotion)));

        return promotionRepository.findAllDto();
    }

    @Override
    public void updateP(PromotionDto dto, Long id) {
        Promotion promotion = dto.promotion();
        promotion.setId(id);
        promotionRepository.save(promotion);
    }

    @Override
    public void addProduct(PromotionDto promotionDto) {
        promotionDto.getProducts().stream().forEach(x -> {
            x.setPromotion(Promotion.builder().id(promotionDto.getId()).build());
            productRepository.save(x);
        });


    }

    @Override
    public void addProductd(List<Long> list, Long id) {
        List<Product> l = new ArrayList<>();
        for (Long x : list) {
            l.add(productRepository.getReferenceById(x));
        }
        l.stream().forEach(x -> {
            x.setPromotion(Promotion.builder().id(id).build());
            productRepository.save(x);
        });

    }


}
