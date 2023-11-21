package com.poly.datn.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poly.datn.entity.Brand;
import com.poly.datn.entity.Category;
import com.poly.datn.entity.Color;
import com.poly.datn.entity.Image;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.entity.Size;
import com.poly.datn.entity.TypeProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDto {
    private Long id;
    private String maSp;
    private String name;
    private String image;
    @JsonIgnore
    private Integer quantity;
    @JsonIgnore
    private String status;
    @JsonIgnore
    private String fabric;
    @JsonIgnore
    private String descriptionProduct;
    @JsonIgnore
    private String manual;
    @JsonIgnore
    private String style;
    @JsonIgnore
    private String pattern;
    @JsonIgnore
    private Category category;
    @JsonIgnore
    private Brand brand;
    @JsonIgnore
    private TypeProduct typeProduct;
    @JsonIgnore
    private List<Image> images;
    @JsonIgnore
    private String qrCode;


    private List<ColorSizeVertion> vertions;
    private List<Color> colors;
    private List<Size> sizes;

    public ProductDto(Product x) {
        Optional<Image> img = (Optional<Image>) x.getImages().stream().findFirst();
        Image image = img.orElse(new Image(new Product(), "https://phutungnhapkhauchinhhang.com/wp-content/uploads/2020/06/default-thumbnail.jpg", ""));
        this.id = x.getId();
        this.maSp = x.getMaSp();
        this.name = x.getProductName();
        this.image = image.getUrl();
        this.typeProduct = x.getTypeProduct();
        this.quantity = x.getProductDetails().stream().mapToInt(ProductDetail::getQuantity).sum();
        this.status = x.getStatus();
        this.images = x.getImages();
        this.descriptionProduct = x.getDescription().getDescriptionProduct();
        this.fabric = x.getDescription().getFabric();
        this.manual = x.getDescription().getManual();
        this.style = x.getDescription().getStyle();
        this.pattern = x.getDescription().getPattern();
        this.category = x.getTypeProduct().getCategory();
        this.brand = x.getBrand();
    }


}
