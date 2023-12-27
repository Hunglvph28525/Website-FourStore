package com.poly.datn.service;

import com.poly.datn.dto.ProductDto;
import com.poly.datn.dto.ProductRequest;
import com.poly.datn.entity.Category;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.TypeProduct;
import com.poly.datn.util.MessageUtil;

import java.util.List;

public interface ProductService {

    ProductRequest getProduct(Long id);

    MessageUtil add(ProductRequest product);

    Product detail(Long id);

    List<ProductDto> getAll(String status, Long category, Long type, Long brand);


    MessageUtil update(ProductRequest request, Long id);

    MessageUtil save(Long id);

    ProductDto getDetail(Long id);

}
