package com.poly.datn.service.impl;

import com.poly.datn.entity.Product;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.repository.ProductDetailRepository;
import com.poly.datn.repository.ProductRepository;
import com.poly.datn.service.ProductDetailService;
import com.poly.datn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

   @Autowired
    ProductDetailRepository productDetailRepository;

    @Override
    public List<ProductDetail> getAll() {
        return null;
    }

    @Override
    public Page<ProductDetail> phanTrang(Integer pageNum, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNum, pageNo);
        return productDetailRepository.findAll(pageable);

    }

    @Override
    public void add(ProductDetail product) {
        productDetailRepository.save(product);

    }

    @Override
    public ProductDetail detail(Long id) {
        return null;
    }
}
