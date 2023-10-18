package com.poly.datn.service.impl;

import com.poly.datn.dto.ProductDetailDto;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.repository.ProductDetailRepository;
import com.poly.datn.service.ProductDetailService;
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

    @Override
    public List<ProductDetailDto> getDetail(Long id) {
        return productDetailRepository.getDetailProduct(id);
    }

    @Override
    public void update(Long id, Integer quantity) {
        ProductDetail productDetail = productDetailRepository.getReferenceById(id);
        productDetail.setQuantity(quantity);
        if (productDetail.getQuantity()<=0){
            productDetail.setStatus("0");
        }else {
            productDetail.setStatus("1");
        }
        productDetailRepository.save(productDetail);
    }
}
