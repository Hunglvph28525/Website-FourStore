package com.poly.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
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

    @OneToMany(mappedBy = "promotion")
    private List<Product> products;

    @Column(name = "discount_name",columnDefinition = ("nvarchar(255)"))
    private String discountName;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "discount_value")
    private Integer discountValue; // giá trị giảm

    @Column(name = "discount_type")
    private Boolean discountType;  // Kiểu giảm giá

    @Column(name = "create_date")
    private LocalDateTime cteateDate;

    @Column(name = "edit_date")
    private LocalDateTime editDate;

    @Column(name = "status",columnDefinition = ("nvarchar(255)"))
    private String status;
}
