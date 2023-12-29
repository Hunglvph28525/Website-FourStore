package com.poly.datn.dto;

import com.poly.datn.util.Fomater;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailDto {
    private Long id;
    private ProductDetailDto product;
    private Integer quantity;
    private Double price;
    private String priceFomat;

}
