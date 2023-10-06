package com.poly.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

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

    @Column(name = "discount_name",columnDefinition = ("nvarchar(255)"))
    private String discountName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "discount_value")
    private Integer discountValue; // giá trị giảm

    @Column(name = "discount_type")
    private Boolean discountType;  // Kiểu giảm giá

    @Column(name = "create_date")
    private Date cteateDate;

    @Column(name = "edit_date")
    private Date editDate;

    @Column(name = "status",columnDefinition = ("nvarchar(255)"))
    private String status;
}
