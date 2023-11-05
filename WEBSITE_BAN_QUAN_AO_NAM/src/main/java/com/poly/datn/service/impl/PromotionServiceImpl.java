package com.poly.datn.service.impl;

import com.poly.datn.dto.PromotionDto;
import com.poly.datn.entity.Promotion;
import com.poly.datn.repository.PromotionRepository;
import com.poly.datn.service.PromotionService;
import com.poly.datn.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    @Override
    public MessageUtil add(PromotionDto dto) {
        if (promotionRepository.existsByGiftCodeOrDiscountName(dto.getGiftCode(), dto.getName())) {
            return new MessageUtil(0, "Thất bại phiếu giảm giá này đã có", "bg-danger", dto);
        }
        Promotion promotion = dto.promotion();
        promotion.setGiftCode(generateRandomString(8));
        promotion.setCteateDate(LocalDateTime.now());
        promotionRepository.save(promotion);
        return new MessageUtil(1, "Thêm thành công", "bg-success", promotion);
    }

    @Override
    public MessageUtil update(PromotionDto dto, Long id) {
        String text = "";
        if (LocalDateTime.now().isBefore(dto.getStartDate())) {
            text = "0";//chưa diễn ra
        } else if (LocalDateTime.now().isAfter(dto.getEndDate())) {
            text = "2";// Kết thúc
        } else {
            text = "1";// đang diễn ra
        }
        Promotion promotion = promotionRepository.getReferenceById(id);
        promotion.setDiscountName(dto.getName());
        promotion.setQuantity(dto.getQuantity());
        promotion.setDiscountValue(dto.getDiscount());
        promotion.setMinValue(dto.getMinValue());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
        promotion.setStatus(text);
        promotionRepository.save(promotion);
        return new MessageUtil(1, "Thêm thành công", "bg-success");
    }

    @Override
    public Object getAll(String status) {
        if (status.equals("-1")) {
            return promotionRepository.findAllDto();
        }
        return promotionRepository.findAllDto(status);
    }

    @Override
    public void jobUpdate() {
        List<Promotion> list = promotionRepository.findAll();
        list.stream().forEach(x -> {
            if (LocalDateTime.now().isBefore(x.getStartDate())) {
                x.setStatus("0"); // Chưa diễn ra
            } else if (LocalDateTime.now().isAfter(x.getEndDate())) {
                x.setStatus("2"); // Đã kết thúc
            } else {
                x.setStatus("1"); // Đang diễn ra
            }
        });
        promotionRepository.saveAll(list);
    }

    @Override
    public Object detail(Long id) {
        return new PromotionDto(promotionRepository.getReferenceById(id));
    }

    private String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }
        return randomString.toString().toUpperCase();
    }
}
