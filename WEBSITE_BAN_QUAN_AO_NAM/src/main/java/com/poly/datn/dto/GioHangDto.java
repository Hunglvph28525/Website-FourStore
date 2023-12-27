package com.poly.datn.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class GioHangDto {
    private Long id;
    private List<CartDetailDto> cartDetails;
    private Double total;
    private Long tongSp;
    private String totalFomat;
}
