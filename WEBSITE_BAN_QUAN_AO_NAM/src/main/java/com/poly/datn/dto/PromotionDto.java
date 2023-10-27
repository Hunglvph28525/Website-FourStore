package com.poly.datn.dto;

import com.poly.datn.entity.Product;
import com.poly.datn.entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Setter
@Getter

public class PromotionDto {

    private Long id;
    private String discountName;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;
    private Integer discountValue;
    private Boolean discountType;
    private Long countProduct;
    private String status;
    private List<Product> products;
    private String time;


    public PromotionDto(Promotion x) {
        String text = "";
        if (LocalDateTime.now().isBefore(x.getStartDate())){
            text = "Chưa bắt đầu";//chưa diễn ra
        }else if(LocalDateTime.now().isAfter(x.getEndDate())){
            text = "Kết thúc";// Kết thúc
        }else {
            text = "Đang diễn ra";// đang diễn ra
        }
        this.id = x.getId();
        this.discountName = x.getDiscountName();
        this.startDate = x.getStartDate();
        this.endDate = x.getEndDate();
        this.discountValue = x.getDiscountValue();
        this.discountType = x.getDiscountType();
        this.countProduct = x.getProducts().stream().count();
        this.status = text;
        this.products = x.getProducts();
        this.time = x.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " - " + x.getEndDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public Promotion promotion() {
        String text = "";
        if (LocalDateTime.now().isBefore(this.startDate)){
            text = "Chưa bắt đầu";//chưa diễn ra
        }else if(LocalDateTime.now().isAfter(this.endDate)){
            text = "Kết thúc";// Kết thúc
        }else {
            text = "Đang diễn ra";// đang diễn ra
        }
        Promotion promotion = new Promotion();
        promotion.setId(this.id);
        promotion.setDiscountName(this.discountName);
        promotion.setStartDate(this.startDate);
        promotion.setEndDate(this.endDate);
        promotion.setDiscountType(this.discountType);
        promotion.setDiscountValue(this.discountValue);
        promotion.setStatus(text);
        promotion.setCteateDate(LocalDateTime.now());
        promotion.setEditDate(LocalDateTime.now());
        promotion.setProducts(this.products);
        return promotion;
    }


}
