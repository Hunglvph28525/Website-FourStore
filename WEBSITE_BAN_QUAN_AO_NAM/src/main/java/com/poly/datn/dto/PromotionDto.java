package com.poly.datn.dto;

import com.poly.datn.entity.Promotion;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Setter
@Getter

public class PromotionDto {
    private Long id;


    private String discountName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer discountValue;
    private Boolean discountType;
    private Long product;
    private String status;

    private Integer quantity;

    public Promotion promotion() {
        Promotion promotion = new Promotion();
        promotion.setId(this.id);
        promotion.setDiscountName(this.discountName);
        promotion.setStartDate(this.startDate);
        promotion.setEndDate(this.endDate);
        promotion.setDiscountType(this.discountType);
        promotion.setDiscountValue(this.discountValue);
        promotion.setQuantity(this.quantity);
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
        this.quantity = x.getQuantity();
    }


}
