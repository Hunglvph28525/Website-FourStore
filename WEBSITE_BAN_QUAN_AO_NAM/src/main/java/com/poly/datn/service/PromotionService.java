package com.poly.datn.service;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.Promotion;
import com.poly.datn.util.MessageUtil;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public interface PromotionService {

    MessageUtil add(PromotionDto dto);

    MessageUtil update(PromotionDto dto,Long id);

    Object getAll(String status);

    void jobUpdate();
}
