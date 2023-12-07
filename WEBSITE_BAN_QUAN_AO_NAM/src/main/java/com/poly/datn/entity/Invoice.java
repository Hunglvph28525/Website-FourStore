package com.poly.datn.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "Invoices")
public class Invoice {
    @Id
    @Column(name = "code_bill")
    private String codeBill; // Mã hóa đơn

    @ManyToOne
    @JoinColumn(name = "id_payment", referencedColumnName = "id")
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_promotion")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_shipping_address", referencedColumnName = "id")
    private ShippingAddress shippingAddress;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private Set<Transaction> transactions;

    @DateTimeFormat(pattern = "yyyy-MM-dd'-'HH:mm")
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "shipping_cost")// (Phí vận chuyển)
    private BigDecimal shippingCost;

    @Column(name = "shipping")// (giao hay không giao)
    private Boolean shipping;

    @Column(name = "total") // tổng tiền sản phẩm
    private BigDecimal total;

    @Column(name = "gia_giam") // tiền giảm giá
    private BigDecimal giaGiam;

    @Column(name = "grand_total") // tổng tiền hóa đơn = tổng tiền sản phẩm + phí ship
    private BigDecimal grandTotal;

    @Column(name = "shipping_date") // ngày giao hàng
    private LocalDateTime shippingDate;

    @Column(name = "note", columnDefinition = ("nvarchar(255)"))
    private String note;

    @Column(name = "sdt_nhan", columnDefinition = ("nvarchar(12)"))
    private String sdtNhan;

    @Column(name = "status", columnDefinition = ("nvarchar(255)"))
    private String status;

    @Column(name = "type", columnDefinition = ("nvarchar(255)"))
    private String type;

    @DateTimeFormat(pattern = "yyyy-MM-dd'-'HH:mm")
    @Column(name = "payment_date") // ngày thanh toán
    private LocalDateTime paymentDate;

    @Column(name = "payment_info", columnDefinition = ("nvarchar(255)"))
    private String paymentInfo;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "user_pay")
    private BigDecimal userPay;

    @Column(name = "tien_thoi")
    private BigDecimal tienThoi;
}
