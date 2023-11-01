package com.poly.datn.service;

import com.poly.datn.dto.ProductDto;
import com.poly.datn.dto.ProductRequest;
import com.poly.datn.entity.Category;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.TypeProduct;

import java.util.List;

public interface ProductService {

    ProductRequest getProduct(Long id);

    Product add(ProductRequest product);

    Product detail(Long id);

    List<ProductDto> getAll(String status, Long category, Long type, Long brand);

    List<Product> getProductNoPromotion();

    void update(ProductRequest request, Long id);

    void save(Long id);

    void updatePromotion(Long id, Category category, TypeProduct type, List<Long> products);
}
