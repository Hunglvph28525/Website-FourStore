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
    private String paymentStatus;
    private List<InvoiceDetailDto> product;
    private Integer total;
    private Integer giaGiam;
    private Integer shippingCost;
    private Integer grandTotal;
    private User user;
    private Long tongSp;
    private String pgg;


    public InvoiceDto(Invoice x) {
        this.codeBill = x.getCodeBill();
        this.type = x.getType().equals("1") ? "Tại quầy" : "Online";
        this.status = x.getStatus();
        this.customer = x.getUser() == null ? "Khách bán lẻ" : x.getUser().getName();
        this.sdt = x.getSdtNhan();
        this.address = address(x.getShippingAddress());
        this.paymentMethod = x.getPaymentMethod() ==null ?"-" : x.getPaymentMethod().getMethodName();
        this.paymentDate = x.getPaymentDate();
        this.paymentStatus = x.getPaymentStatus() == null ? "-" : x.getPaymentStatus().equals("1") ? "Đã thanh toán":"Chưa thanh toán";
        this.paymentInfo = x.getPaymentInfo();
        this.total = x.getTotal().intValue();
        this.giaGiam = x.getGiaGiam().intValue();
        this.shippingCost = x.getShippingCost().intValue();
        this.grandTotal = x.getGrandTotal().intValue();
        this.pgg = x.getPromotion() == null? null : x.getPromotion().getGiftCode();
        this.user = x.getUser() == null ? new User(): x.getUser();
    }

    private String address(ShippingAddress x) {
        if (x==null)return "chưa có";
        return x.getStreet() + ", " + x.getWard() + ", " + x.getDistrict() + ", " + x.getProvince();
    }
}
