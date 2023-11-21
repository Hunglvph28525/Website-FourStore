package com.poly.datn.dto;

import com.poly.datn.entity.Image;
import com.poly.datn.entity.InvoiceDetail;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class InvoiceDetailDto {
    private Long id;
    private String name;
    private String color;
    private String size;
    private Integer quantity;
    private Integer price;
    private String img;
    private ProductDetail productDetail;

    public InvoiceDetailDto(InvoiceDetail x) {
        Optional<Image> img = (Optional<Image>) x.getInvoiceId().getProductDetail().getProduct().getImages().stream().findFirst();
        Image image = img.orElse(new Image(new Product(), "https://phutungnhapkhauchinhhang.com/wp-content/uploads/2020/06/default-thumbnail.jpg", ""));
        this.id = x.getInvoiceId().getProductDetail().getId();
        this.name = x.getInvoiceId().getProductDetail().getProduct().getProductName();
        this.color = x.getInvoiceId().getProductDetail().getColor().getName();
        this.size = x.getInvoiceId().getProductDetail().getSize().getName();
        this.quantity = x.getQuantity();
        this.price = x.getQuantity() * x.getInvoiceId().getProductDetail().getPrice().intValue();
        this.img = image.getUrl();
        this.productDetail = x.getInvoiceId().getProductDetail();
    }

}
