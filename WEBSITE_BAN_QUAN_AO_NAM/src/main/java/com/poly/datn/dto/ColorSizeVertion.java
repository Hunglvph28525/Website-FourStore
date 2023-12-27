package com.poly.datn.dto;

import com.poly.datn.entity.Color;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.entity.Size;
import com.poly.datn.util.Fomater;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ColorSizeVertion {
    private Long id;
    private Color color;
    private Size size;
    private Integer quantity;
    private Long price;
    private String priceFomat;

    public ColorSizeVertion(ProductDetail x) {
        this.id = x.getId();
        this.color = x.getColor();
        this.size = x.getSize();
        this.quantity = x.getQuantity();
        this.price = x.getPrice().longValue();
        this.priceFomat = Fomater.fomatTien().format(x.getPrice());
    }
}
