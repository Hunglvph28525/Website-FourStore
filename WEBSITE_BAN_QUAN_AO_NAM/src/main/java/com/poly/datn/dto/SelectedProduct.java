package com.poly.datn.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class SelectedProduct {
    private Long productId;
    private Integer quantity;
}
