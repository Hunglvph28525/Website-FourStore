package com.poly.datn.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "Promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<Invoice> invoices;

    @Column(name = "discount_name", columnDefinition = ("nvarchar(255)"))
    private String discountName;

    @Column(name = "gift_code", columnDefinition = "varchar(8)", unique = true)
    private String giftCode;

    @Column(name = "min_value")
    private BigDecimal minValue;

    @Column(name = "max_value")
    private BigDecimal maxValue;


    @Max(10000)
    @Column(name = "quantity")
    private Integer quantity;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "discount_value")
    @Min(0)
    @Max(90)
    private Integer discountValue; // giá trị giảm


    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "create_date")
    private LocalDateTime cteateDate;


    @Column(name = "status", columnDefinition = ("nvarchar(255)"))
    private String status;

}
