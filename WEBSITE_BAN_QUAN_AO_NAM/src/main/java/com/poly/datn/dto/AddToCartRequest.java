package com.poly.datn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class AddToCartRequest {

    private String codeBill;

    private Long colorId;

    private Long productId;

    private Integer quantity;

    private Long sizeId;

}
