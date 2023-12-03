package com.poly.datn.dto;


import com.poly.datn.entity.Image;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.ProductDetail;
import lombok.*;

import java.util.Optional;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProductDetailDto {
    private Long id;
    private String name;
    private Integer quantity;
    private Long price;
    private String img;

    public ProductDetailDto(ProductDetail x) {
        Optional<Image> img = (Optional<Image>) x.getProduct().getImages().stream().findFirst();
        Image image = img.orElse(new Image(new Product(), "https://phutungnhapkhauchinhhang.com/wp-content/uploads/2020/06/default-thumbnail.jpg", ""));
        this.id = x.getId();
        this.name = x.getProduct().getProductName() + "[" + x.getColor().getName() + " - " + x.getSize().getName() + "]";
        this.quantity = x.getQuantity();
        this.price = x.getPrice().longValue();
        this.img = image.getUrl();
    }
}
