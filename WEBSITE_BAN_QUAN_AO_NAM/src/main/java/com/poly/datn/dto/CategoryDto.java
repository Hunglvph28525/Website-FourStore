package com.poly.datn.dto;

import com.poly.datn.entity.Category;
import com.poly.datn.entity.TypeProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CategoryDto {
    private Long id;
    private String name;
    private Set<TypeProduct> typeProducts;
    private Long countProduct;
    private Long countType;
    private String status;

    public CategoryDto(Category x) {
        this.id = x.getId();
        this.name = x.getCategoryName();
        this.typeProducts = x.getTypeProducts();
        this.countType = x.getTypeProducts().stream().count();
        Long count = 0l;
        for (TypeProduct n : x.getTypeProducts()) {
            if (n.getProducts() != null){
                count += n.getProducts().stream().count();
            }

        }
        this.countProduct = count;
    }

    public Category category() {
        return new Category(this.id, this.typeProducts, this.name, this.status);
    }
}
