package com.poly.datn.dto;

import com.poly.datn.entity.Address;
import com.poly.datn.entity.PaymentMethod;
import com.poly.datn.entity.Promotion;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class OrderDetailDto {
    private Address address;
    private List<Address> addresses;
    private List<CartDetailDto> products;
    private Promotion promotion;
    private Integer total;
    private Integer giaGiam;
    private Boolean shipping;
    private Integer shippingCost;
    private Integer grandTotal;
    private PaymentMethod paymentMethod;
    private String name;
    private String sdt;

    private String totalFomat;
    private String giaGiamFomat;
    private String shippingCostFomat;
    private String grandTotalFomat;
}
