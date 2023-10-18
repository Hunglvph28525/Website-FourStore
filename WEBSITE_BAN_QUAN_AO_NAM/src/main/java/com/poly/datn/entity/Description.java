package com.poly.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "Descriptions")
public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "description", fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "fabric",columnDefinition = ("nvarchar(255)"))
    private String fabric; // chất liệu

    @Column(name = "description_product",columnDefinition = ("nvarchar(255)"))
    private String descriptionProduct; // mô tả sản phẩm

    @Column(name = "manual",columnDefinition = ("nvarchar(255)"))
    private String manual;   //hướng dẫn giặt

    @Column(name = "style",columnDefinition = ("nvarchar(255)"))
    private String style; //kiểu dáng

    @Column(name = "pattern",columnDefinition = ("nvarchar(255)"))   //  Họa tiết
    private String pattern;

    public Description(String fabric, String descriptionProduct, String manual, String style, String pattern) {
        this.fabric = fabric;
        this.descriptionProduct = descriptionProduct;
        this.manual = manual;
        this.style = style;
        this.pattern = pattern;
    }
}
