package com.poly.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Address")
public class Address {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    @Column(name = "province",columnDefinition = ("nvarchar(255)"))
    private String province;

    @Column(name = "district",columnDefinition = ("nvarchar(255)"))
    private String district;

    @Column(name = "ward",columnDefinition = ("nvarchar(255)"))
    private String ward;

    @Column(name = "street",columnDefinition = ("nvarchar(255)"))
    private String street;

    @Column(name = "status",columnDefinition = ("nvarchar(255)"))
    private String status;
}
