package com.poly.datn.dto;

import com.poly.datn.entity.Category;
import com.poly.datn.entity.TypeProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class TtpeProductDto {
    private Long id;
    private TypeProduct typeProduct;
    private Category category;
    private Long count;

    public TtpeProductDto(TypeProduct typeProduct) {
        this.typeProduct = typeProduct;
        this.count = typeProduct.getProducts().stream().count();
        this.id = typeProduct.getId();
        this.category = typeProduct.getCategory();
    }
}
