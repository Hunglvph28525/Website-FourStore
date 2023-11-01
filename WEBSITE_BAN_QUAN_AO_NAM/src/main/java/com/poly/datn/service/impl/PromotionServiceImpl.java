package com.poly.datn.service.impl;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.Promotion;
import com.poly.datn.repository.ProductRepository;
import com.poly.datn.repository.PromotionRepository;
import com.poly.datn.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    ProductRepository productRepository;


    @Override
    public void add(Promotion dto, List<Long> products) {
        dto = promotionRepository.save(dto);
        Product product = new Product();
        if (products == null || products.isEmpty()) return;
        for (Long x : products) {
            product = productRepository.getReferenceById(x);
            product.setPromotion(dto);
            productRepository.save(product);
        }
    }


    @Override
    public Promotion detail(Long id) {
        return promotionRepository.getById(id);
    }


    @Override
    public void addProduct(List<Long> listPrd, Long id) {
        for (Long x : listPrd) {
            Product product = productRepository.getReferenceById(x);
            product.setPromotion(promotionRepository.getReferenceById(id));
            productRepository.save(product);
        }
    }

    @Override
    public void delete(Long id) {
        List<Product> list = productRepository.getProducByPromotion(id);
        list.stream().forEach(x -> {
            x.setPromotion(null);
            productRepository.save(x);
        });
        promotionRepository.deleteById(id);
    }

    @Override
    public void update(PromotionDto dto) {
        Promotion promotion = promotionRepository.getReferenceById(dto.getId());
        promotion.setDiscountValue(dto.getDiscountValue());
        promotion.setDiscountName(dto.getDiscountName());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
        promotion.setEditDate(LocalDateTime.now());
        promotion.setStatus(dto.promotion().getStatus());
        promotionRepository.save(promotion);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.getReferenceById(id);
        product.setPromotion(null);
        productRepository.save(product);
    }

    @Override
    public void jopUpdateStatus() {
        List<Promotion> promotions = promotionRepository.findAll();
        for (Promotion x : promotions) {
            if (LocalDateTime.now().isBefore(x.getStartDate())) {
                x.setStatus("0"); //chưa diễn ra
            } else if (LocalDateTime.now().isAfter(x.getEndDate())) {
                x.setStatus("2");
            } else {
                x.setStatus("1");// đang diễn ra
            }
        }
        promotions = promotionRepository.saveAll(promotions);
        List<Product> products = new ArrayList<>();
        for (Promotion x : promotions) {
            if (x.getStatus().equals("2")){
                products = x.getProducts();
                products.stream().forEach( o -> o.setPromotion(null));
                productRepository.saveAll(products);
            }
        }
    }

    @Override
    public Object getAll(String status, LocalDateTime start, LocalDateTime end) {
        if (status.equals("-1") && start == null && end == null) {
            return promotionRepository.findAllDto();
        }
        return promotionRepository.findAllDto(status, start, end);
    }
}
