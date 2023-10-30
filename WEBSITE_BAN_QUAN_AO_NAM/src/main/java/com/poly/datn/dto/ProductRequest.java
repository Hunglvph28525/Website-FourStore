package com.poly.datn.dto;

import com.poly.datn.entity.Brand;
import com.poly.datn.entity.Color;
import com.poly.datn.entity.Description;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.entity.Size;
import com.poly.datn.entity.TypeProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductRequest {
    private Long id;
    private String maSp;
    private String name;
    private BigDecimal price;
    private String status;
    private String fabric;
    private String descriptionProduct;
    private String manual;
    private String style;
    private String pattern;
    private TypeProduct typeProduct;
    private Brand brand;
    private List<MultipartFile> files;

    private List<ProductDetail> productDetails;
    private List<Color> colors;
    private List<Size> sizes;

    public Product product() {
        Product product = Product.builder()
                .typeProduct(this.typeProduct)
                .description(Description.builder()
                        .descriptionProduct(this.descriptionProduct)
                        .fabric(this.fabric)
                        .manual(this.manual)
                        .pattern(this.pattern)
                        .style(this.style)
                        .build())
                .brand(this.brand)
                .productName(this.name)
                .price(this.price)
                .status(this.getStatus())
                .maSp(this.maSp)
                .build();
        return product;
    }

    public ProductRequest(Product x) {
        this.id = x.getId();
        this.maSp = x.getMaSp();
        this.name = x.getProductName();
        this.price = x.getPrice();
        this.status = x.getStatus();
        this.fabric = x.getDescription().getFabric();
        this.descriptionProduct = x.getDescription().getDescriptionProduct();
        this.manual = x.getDescription().getManual();
        this.style = x.getDescription().getStyle();
        this.pattern = x.getDescription().getPattern();
        this.typeProduct = x.getTypeProduct();
        this.brand = x.getBrand();
    }
}
