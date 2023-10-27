package com.poly.datn.service;

import com.poly.datn.dto.ProductDto;
import com.poly.datn.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductDto getProductById(Long id);

    Product add(ProductDto product);

    Product detail(Long id);

    List<ProductDto> getAll();

    void updateProduct(ProductDto dto,Long sp);

    List<Product> getProductNoPromotion();

    Product save(Product product);
}
