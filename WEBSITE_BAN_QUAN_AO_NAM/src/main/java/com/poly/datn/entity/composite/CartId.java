package com.poly.datn.entity.composite;

import com.poly.datn.entity.Cart;
import com.poly.datn.entity.ProductDetail;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Embeddable
public class CartId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_product")
    private ProductDetail productDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_cart")
    private Cart cart;
}
