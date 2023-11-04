package com.poly.datn.dto;

import com.poly.datn.entity.Brand;
import com.poly.datn.entity.Category;
import com.poly.datn.entity.Color;
import com.poly.datn.entity.Description;
import com.poly.datn.entity.Image;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.entity.Size;
import com.poly.datn.entity.TypeProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDto {

    private Long id;
    private String maSp;
    private String name;
    private String image;
    private Integer quantity;
    private String status;
    private String fabric;
    private String descriptionProduct;
    private String manual;
    private String style;
    private String pattern;
    private Category category;
    private Brand brand;
    private TypeProduct typeProduct;
    private List<Image> images;
    private String qrCode;


    public ProductDto(Product x) {
        Optional<Image> img = (Optional<Image>) x.getImages().stream().findFirst();
        Image image = img.orElse(new Image(new Product(),"https://phutungnhapkhauchinhhang.com/wp-content/uploads/2020/06/default-thumbnail.jpg",""));
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
