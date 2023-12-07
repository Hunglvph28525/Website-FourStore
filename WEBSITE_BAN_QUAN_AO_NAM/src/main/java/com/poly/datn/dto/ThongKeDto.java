package com.poly.datn.dto;

import com.poly.datn.entity.composite.InvoiceId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ThongKeDto {
    private Long id;
    private String productName;
    private String url;
    private Integer quantity;
    private Double price;
    private Double sumPrice;

}
