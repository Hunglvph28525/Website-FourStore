package com.poly.datn.dto;

import com.poly.datn.entity.Invoice;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.entity.ShippingAddress;
import com.poly.datn.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class InvoiceDto {
    private String codeBill;
    private String type;
    private String status;
    private String customer;
    private String sdt;
    private String address;
    private String paymentMethod;
    @DateTimeFormat(pattern = "HH:mm' - 'dd/MM/yyyy")
    private LocalDateTime paymentDate;
    private String paymentInfo;
    private List<InvoiceDetailDto> product;
    private BigDecimal total;
    private BigDecimal giaGiam;
    private BigDecimal shippingCost;
    private BigDecimal grandTotal;
    private User user;


    public InvoiceDto(Invoice x) {
        this.codeBill = x.getCodeBill();
        this.type = x.getType().equals("1") ? "Tại quầy" : "Online";
        this.status = x.getStatus();
        this.customer = x.getUser() == null ? "Khách bán lẻ" : x.getUser().getName();
        this.sdt = x.getSdtNhan();
        this.address = address(x.getShippingAddress());
        this.paymentMethod = x.getPaymentMethod() ==null ?"-" : x.getPaymentMethod().getMethodName();
        this.paymentDate = x.getPaymentDate();
        this.paymentInfo = x.getPaymentInfo() ==null? "-" : x.getPaymentInfo().equals("1") ? "Đã thanh toán":"Chưa thanh toán";
        this.total = x.getTotal();
        this.giaGiam = x.getGiaGiam();
        this.shippingCost = x.getShippingCost();
        this.grandTotal = x.getGrandTotal();
        this.user = x.getUser();
    }

    private String address(ShippingAddress x) {
        if (x==null)return "chưa có";
        return x.getStreet() + ", " + x.getWard() + ", " + x.getDistrict() + ", " + x.getProvince();
    }
}
