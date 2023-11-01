package com.poly.datn.service;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.Promotion;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public interface PromotionService {

    void add(Promotion dto, List<Long> products);

    Promotion detail(Long id);

    void addProduct(List<Long> listPrd, Long id);


    void delete(Long id);

    void update(PromotionDto dto);

    void deleteProduct(Long id);

    void jopUpdateStatus();

    Object getAll(String status, LocalDateTime start, LocalDateTime end);
}
