package com.poly.datn.service.impl;

import com.poly.datn.entity.Product;
import com.poly.datn.repository.ProductRepository;
import com.poly.datn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> phanTrang(Integer pageNum, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNum, pageNo);
        return productRepository.findAll(pageable);

    }

    @Override
    public void add(Product product) {

        productRepository.save(product);


    }

    @Override
    public Product detail(Long id) {
        return productRepository.getById(id);
    }
}
