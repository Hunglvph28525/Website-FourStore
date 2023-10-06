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
@Table(name = "ShippingAddress")
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "shippingAddress", fetch = FetchType.LAZY)
    private Invoice invoice;

    @Column(name = "province", columnDefinition = ("nvarchar(255)"))
    private String province;

    @Column(name = "district", columnDefinition = ("nvarchar(255)"))
    private String district;

    @Column(name = "ward", columnDefinition = ("nvarchar(255)"))
    private String ward;

    @Column(name = "street", columnDefinition = ("nvarchar(255)"))
    private String street;
}
