package com.poly.datn.dto;

import com.poly.datn.entity.Product;
import com.poly.datn.entity.Promotion;
import com.poly.datn.entity.TypeProduct;
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
    private Long countProduct;
    private String status;
    private TypeProduct typeProduct;
    private List<Product> products;
    private String time;
    private String description;


    public PromotionDto(Promotion x) {
        String text = "";
        if (LocalDateTime.now().isBefore(x.getStartDate())){
            text = "0";//chưa diễn ra
        }else if(LocalDateTime.now().isAfter(x.getEndDate())){
            text = "2";// Kết thúc
        }else {
            text = "1";// đang diễn ra
        }
        this.id = x.getId();
        this.discountName = x.getDiscountName();
        this.startDate = x.getStartDate();
        this.endDate = x.getEndDate();
        this.discountValue = x.getDiscountValue();
        this.countProduct = x.getProducts().stream().count();
        this.status = text;
        this.products = x.getProducts();
        this.time = x.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " - " + x.getEndDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.description = x.getDescription();
    }

    public Promotion promotion() {
        String text = "";
        if (LocalDateTime.now().isBefore(this.startDate)){
            text = "0";//chưa diễn ra
        }else if(LocalDateTime.now().isAfter(this.endDate)){
            text = "2";// Kết thúc
        }else {
            text = "1";// đang diễn ra
        }
        Promotion promotion = new Promotion();
        promotion.setId(this.id);
        promotion.setDiscountName(this.discountName);
        promotion.setStartDate(this.startDate);
        promotion.setEndDate(this.endDate);
        promotion.setDiscountValue(this.discountValue);
        promotion.setStatus(text);
        promotion.setCteateDate(LocalDateTime.now());
        promotion.setProducts(this.products);
        promotion.setDescription(this.description);
        return promotion;
    }


}
