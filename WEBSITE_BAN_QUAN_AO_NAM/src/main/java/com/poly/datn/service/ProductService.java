package com.poly.datn.service;

import com.poly.datn.entity.Color;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.Promotion;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Page<Product> phanTrang(Integer pageNum, Integer pageNo);

    void add(Product product);

    Product detail(Long id);
}
