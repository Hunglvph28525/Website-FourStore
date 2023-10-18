package com.poly.datn.dto;

import com.poly.datn.entity.Color;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductDetailDto {
    private Long id;
    private Product product;
    private Size size;
    private Color color;
    private Integer quantity;

    public ProductDetailDto(ProductDetail x) {
        this.id = x.getId();
        this.product = x.getProduct();
        this.size = x.getSize();
        this.color = x.getColor();
        this.quantity = x.getQuantity();
    }
}
