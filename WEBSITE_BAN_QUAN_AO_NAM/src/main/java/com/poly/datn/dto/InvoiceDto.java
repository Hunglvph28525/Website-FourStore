package com.poly.datn.dto;

import com.poly.datn.entity.Invoice;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.entity.ShippingAddress;
import com.poly.datn.entity.User;
import com.poly.datn.util.Fomater;
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
    @DateTimeFormat(pattern = "HH:mm' - 'dd/MM/yyyy")
    private LocalDateTime createDate;
    private String paymentInfo;
    private String paymentStatus;
    private List<InvoiceDetailDto> product;
    private Integer total;
    private String totalFomat;
    private Integer giaGiam;
    private String giaGiamFomat;
    private Boolean shipping;
    private Integer shippingCost;
    private String shippingCostFomat;
    private Integer grandTotal;
    private String grandTotalFomat;
    private User user;
    private Long tongSp;
    private String pgg;


    public InvoiceDto(Invoice x) {
        this.codeBill = x.getCodeBill();
        this.type = x.getType();
        this.status = x.getStatus();
        this.customer = x.getUser() == null ? "Khách bán lẻ" : x.getUser().getName();
        this.sdt = x.getSdtNhan();
        this.address = x.getShipping() == true ? address(x.getShippingAddress()) : "Không";
        this.paymentMethod = x.getPaymentMethod() == null ? "-" : x.getPaymentMethod().getMethodName();
        this.paymentDate = x.getPaymentDate();
        this.paymentStatus = x.getPaymentStatus();
        this.paymentInfo = x.getPaymentInfo();
        this.total = x.getTotal().intValue();
        this.giaGiam = x.getGiaGiam().intValue();
        this.shippingCost = x.getShippingCost().intValue();
        this.grandTotal = x.getGrandTotal().intValue();
        this.pgg = x.getPromotion() == null ? null : x.getPromotion().getGiftCode();
        this.user = x.getUser() == null ? new User() : x.getUser();
        this.shipping = x.getShipping();
        this.createDate = x.getCreateDate();
        this.totalFomat = Fomater.fomatTien().format(this.total);
        this.giaGiamFomat = Fomater.fomatTien().format(this.giaGiam);
        this.grandTotalFomat = Fomater.fomatTien().format(this.grandTotal);
        this.shippingCostFomat = Fomater.fomatTien().format(this.shippingCost);


    }

    private String address(ShippingAddress x) {
        if (x == null) return "Không";
        return x.getStreet() + ", " + x.getWard() + ", " + x.getDistrict() + ", " + x.getProvince();
    }
}
