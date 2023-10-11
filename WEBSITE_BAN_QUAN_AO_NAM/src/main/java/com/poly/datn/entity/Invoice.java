package com.poly.datn.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
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

    @ManyToOne
    @JoinColumn(name = "id_user",referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_shipping_address", referencedColumnName = "id")
    private ShippingAddress shippingAddress;

    @OneToMany(mappedBy = "invoice",fetch = FetchType.LAZY)
    private Set<Transaction> transactions;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "shipping_cost")// (Phí vận chuyển)
    private BigDecimal shippingCost;


    @Column(name = "total") // tổng tiền sản phẩm
    private BigDecimal total;

    @Column(name = "grand_total") // tổng tiền hóa đơn = tổng tiền sản phẩm + phí ship
    private BigDecimal grandTotal;

    @Column(name = "payment_date") // ngày thanh toán
    private LocalDateTime paymentDate;

    @Column(name = "shipping_date") // ngày giao hàng
    private Date shippingDate;

    @Column(name = "note",columnDefinition = ("nvarchar(255)"))
    private String note;

    @Column(name = "status",columnDefinition = ("nvarchar(255)"))
    private String status;

}
