package com.poly.datn.dto;

import com.poly.datn.entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Setter
@Getter

public class PromotionDto {
    private Long id;
    private String giftCode;
    private String name;
    private Integer quantity;
    private Integer discount;
    private BigDecimal minValue;
    private Integer minValue1;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createDate;
    private String status;
    private String time;


    public PromotionDto(Promotion x) {
        this.id = x.getId();
        this.giftCode = x.getGiftCode();
        this.name = x.getDiscountName();
        this.quantity = x.getQuantity();
        this.discount = x.getDiscountValue();
        this.minValue = x.getMinValue();
        this.minValue1 = x.getMinValue().intValue();
        this.startDate = x.getStartDate();
        this.endDate = x.getEndDate();
        this.status = x.getStatus();
        this.time = x.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " - " + x.getEndDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }


    public Promotion promotion() {
        String text = "";
        if (LocalDateTime.now().isBefore(this.startDate)) {
            text = "0";//chưa diễn ra
        } else if (LocalDateTime.now().isAfter(this.endDate)) {
            text = "2";// Kết thúc
        } else {
            text = "1";// đang diễn ra
        }
        return Promotion.builder()
                .id(this.id)
                .discountName(this.name)
                .giftCode(this.giftCode)
                .minValue(this.minValue)
                .quantity(this.quantity)
                .startDate(this.getStartDate())
                .endDate(this.getEndDate())
                .discountValue(this.discount)
                .status(text)
                .build();
    }
}
