package com.poly.datn.dto;

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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDto {
    private Long id;
    private String productName;
    private String defaultImage;
    private BigDecimal price;
    private TypeProduct typeProduct;
    private MultipartFile[] files;
    private String fabric;
    private String descriptionProduct;
    private String manual;
    private String style;
    private String pattern;
    private String status;
    private List<Color> color;
    private List<Size> size;
    private Integer quantity;
    private List<Image> images;
    private String qrCode;


    public ProductDto(Product x) {
        Optional<Image> img = (Optional<Image>) x.getImages().stream().findFirst();
        Image image = img.orElse(new Image(new Product(),"/assets/images/emtity.png",""));
        this.id = x.getId();
        this.productName = x.getProductName();
        this.defaultImage = image.getUrl();
        this.price = x.getPrice();
        this.typeProduct = x.getTypeProduct();
        this.quantity = x.getProductDetails().stream().mapToInt(ProductDetail::getQuantity).sum();
        this.status = (this.quantity>10?"Còn hàng":this.quantity==0?"Hết hàng":"Sắp hết hàng");
        this.images = x.getImages();
        this.descriptionProduct = x.getDescription().getDescriptionProduct();
        this.fabric = x.getDescription().getFabric();
        this.manual = x.getDescription().getManual();
        this.style = x.getDescription().getStyle();
        this.pattern = x.getDescription().getPattern();
    }
    public Product product() {
        Description description = new Description(this.fabric, this.descriptionProduct, this.manual, this.style, this.pattern);
        Product product = new Product();
        product.setId(this.id);
        product.setTypeProduct(this.typeProduct);
        product.setDescription(description);
        product.setProductName(this.productName);
        product.setPrice(this.price);
        product.setCreateDate(Date.valueOf(LocalDate.now()));
        product.setStatus(this.status);
        product.setCreateDate(Date.valueOf(LocalDate.now()));
        product.setTypeProduct(this.typeProduct);
        return product;
    }
}
