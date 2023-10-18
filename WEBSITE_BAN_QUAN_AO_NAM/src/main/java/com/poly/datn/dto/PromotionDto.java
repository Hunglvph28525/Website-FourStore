package com.poly.datn.dto;

import com.poly.datn.entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class PromotionDto {
    private Long id;
    private String discountName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer discountValue;
    private Boolean discountType;
    private Long product;
    private String status;

    public Promotion promotion(){
        Promotion promotion = new Promotion();
        promotion.setId(this.id);
        promotion.setDiscountName(this.discountName);
        promotion.setStartDate(this.startDate);
        promotion.setEndDate(this.endDate);
        promotion.setDiscountType(this.discountType);
        promotion.setDiscountValue(this.discountValue);
        promotion.setStatus(this.status);
        promotion.setCteateDate(LocalDateTime.now());
        promotion.setEditDate(LocalDateTime.now());
        return promotion;
    }

    public PromotionDto(Promotion x) {
        this.id = x.getId();
        this.discountName = x.getDiscountName();
        this.startDate = x.getStartDate();
        this.endDate = x.getEndDate();
        this.discountValue = x.getDiscountValue();
        this.discountType = x.getDiscountType();
        this.product = x.getProducts().stream().count();
        this.status = x.getStatus();
    }
}
