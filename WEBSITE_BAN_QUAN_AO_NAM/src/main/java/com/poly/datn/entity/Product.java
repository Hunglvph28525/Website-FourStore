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
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", referencedColumnName = "id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_product", referencedColumnName = "id")
    private TypeProduct typeProduct;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_description", referencedColumnName = "id")
    private Description description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_promotion")
    private Promotion promotion;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Reviews> reviews = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductDetail> productDetails;

    @Column(name = "product_name", columnDefinition = ("nvarchar(255)"))
    private String productName;

    @Column(name = "price_import")
    @Min(1)
    private BigDecimal priceImport;
    @Min(1)
    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "qr_code")
    private String qrCode;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "status", columnDefinition = ("nvarchar(255)"))
    private String status;


}
