package com.poly.datn.dto;

import com.poly.datn.entity.Category;
import com.poly.datn.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    private Set<Product> products;
    private Long countProduct;
    private String status;

    public CategoryDto(Category x) {
        this.id = x.getId();
        this.name = x.getCategoryName();
        this.products = x.getProducts();
        this.countProduct = x.getProducts().stream().count();
        this.status = x.getStatus().equals("1")?"Hoạt động":"Ngưng hoạt động";
    }
    public Category category(){
        return new Category(this.id,this.products,this.name,this.status);
    }
}
